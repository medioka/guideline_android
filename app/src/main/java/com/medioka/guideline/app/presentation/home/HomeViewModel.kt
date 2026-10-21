package com.medioka.guideline.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medioka.guideline.app.domain.error.WeatherException
import com.medioka.guideline.app.domain.payload.WeatherPayload
import com.medioka.guideline.app.domain.usecase.GetWeatherUseCase
import com.medioka.guideline.app.presentation.common.location.LocationHelper
import com.medioka.guideline.app.presentation.common.location.LocationResult
import com.medioka.guideline.app.presentation.common.model.DailyForecast
import com.medioka.guideline.app.presentation.common.model.WeatherErrorReason
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/** MVI store: intents update [state] and can emit one-shot [effects]. */
class HomeViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationHelper: LocationHelper,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val effectChannel = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    private var hasStarted = false
    private var lastLocation: WeatherPayload? = null
    private var locationJob: Job? = null
    private var weatherJob: Job? = null

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ScreenStarted -> init()
            is HomeIntent.Retry -> retry()
            is HomeIntent.Refresh -> lastLocation?.let(::loadWeather) ?: requestLocation()
            is HomeIntent.OpenSettings -> emitEffect(HomeEffect.OpenAppSettings)
            is HomeIntent.ForecastSelected -> navigateToForecast(intent.forecast)
            is HomeIntent.LocationPermissionGranted -> resolveLocation()
            is HomeIntent.LocationPermissionDenied -> showLocationError()
            is HomeIntent.LocationResolved -> handleLocationResult(intent.result)
        }
    }

    private fun init() {
        if (!hasStarted) {
            hasStarted = true
            requestLocation()
        }
    }

    private fun retry() {
        if (_state.value.error == WeatherErrorReason.LOCATION && lastLocation == null) {
            requestLocation()
        } else {
            lastLocation?.let(::loadWeather) ?: requestLocation()
        }
    }

    private fun requestLocation() {
        locationJob?.cancel()
        weatherJob?.cancel()
        setLoadingState()
        emitEffect(HomeEffect.RequestLocationPermission)
    }

    private fun resolveLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            try {
                onIntent(HomeIntent.LocationResolved(locationHelper.getCurrentLocation()))
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                onIntent(HomeIntent.LocationResolved(LocationResult.Unavailable))
            }
        }
    }

    private fun handleLocationResult(result: LocationResult) {
        when (result) {
            is LocationResult.Available -> {
                lastLocation = result.location
                loadWeather(result.location)
            }

            LocationResult.PermissionDenied,
            LocationResult.Unavailable,
            -> showError(WeatherErrorReason.LOCATION)
        }
    }

    private fun loadWeather(payload: WeatherPayload) {
        weatherJob?.cancel()
        setLoadingState()
        weatherJob = viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(payload)
                _state.value = weather.toHomeState()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: WeatherException) {
                val reason = when (error) {
                    is WeatherException.LocationUnavailable,
                    is WeatherException.InvalidLocation,
                        -> WeatherErrorReason.LOCATION

                    else -> WeatherErrorReason.NETWORK
                }
                showError(reason)
            }
        }
    }

    private fun showLocationError() {
        showError(WeatherErrorReason.LOCATION)
    }

    private fun showError(reason: WeatherErrorReason) {
        val currentState = _state.value
        if (currentState.currentWeather != null && currentState.isRefreshing) {
            _state.value = currentState.copy(
                isLoading = false,
                isRefreshing = false,
                error = null,
            )
            emitEffect(HomeEffect.ShowRefreshError(reason))
            return
        }

        _state.value = _state.value.copy(
            isLoading = false,
            isRefreshing = false,
            error = reason,
        )
    }

    private fun setLoadingState() {
        val hasWeather = _state.value.currentWeather != null
        _state.value = _state.value.copy(
            isLoading = !hasWeather,
            isRefreshing = hasWeather,
            error = null,
        )
    }

    private fun navigateToForecast(forecast: DailyForecast) {
        val currentWeather = _state.value.currentWeather ?: return
        emitEffect(
            HomeEffect.NavigateToWeatherDay(
                forecast = forecast,
                locationName = currentWeather.locationName,
            ),
        )
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch { effectChannel.send(effect) }
    }
}
