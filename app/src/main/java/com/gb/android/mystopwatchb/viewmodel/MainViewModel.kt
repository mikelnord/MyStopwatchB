package com.gb.android.mystopwatchb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gb.android.mystopwatchb.data.StopwatchState
import com.gb.android.mystopwatchb.data.StopwatchStateCalc
import com.gb.android.mystopwatchb.util.format
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val mutableTicker = MutableStateFlow("")
    val ticker: StateFlow<String> = mutableTicker
    private val stopwatchStateCalc= StopwatchStateCalc()

    private fun clearValue() {
        mutableTicker.value = DEFAULT_TIME
    }

    private var currentState: StopwatchState = StopwatchState.Paused(0)

    fun start() {
        viewModelScope.launch {
                while (isActive) {
                    mutableTicker.value = getStringTimeRepresentation()
                    delay(20)
                }
            }
        currentState = stopwatchStateCalc.calculateRunningState(currentState)
    }

    fun pause() {
        viewModelScope.coroutineContext.cancelChildren()
        currentState = stopwatchStateCalc.calculatePausedState(currentState)
    }

    fun stop() {
        viewModelScope.coroutineContext.cancelChildren()
        clearValue()
        currentState = StopwatchState.Paused(0)
    }

    private fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running -> stopwatchStateCalc.calculate(currentState)
        }
        return format(elapsedTime)
    }

    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }

}