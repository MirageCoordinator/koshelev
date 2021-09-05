package ru.dellirium.koshelev

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dellirium.koshelev.common.NonNullLiveData
import ru.dellirium.koshelev.retrofit.RandomQuote
import ru.dellirium.koshelev.retrofit.RetrofitInstance.query

class MainViewModel : ViewModel() {

    private val quotesList = mutableListOf<RandomQuote>()

    private val _currentQuote = MutableLiveData<RandomQuote>()
    private val _currentQuoteIndex = NonNullLiveData(-1)

    val currentQuote: LiveData<RandomQuote>
        get() = _currentQuote
    val currentQuoteIndex: LiveData<Int>
        get() = _currentQuoteIndex

    init {
        addRandomQuote()
    }

    private fun addRandomQuote() {
        viewModelScope.launch {
            val quote = query.getRandomQuote()
            quote.body()?.let {
                quotesList.add(it)
                _currentQuote.value = it
                _currentQuoteIndex.value = _currentQuoteIndex.value + 1
            }
        }
    }

    fun showNextQuote() {
        if (isLastQuote(_currentQuoteIndex.value)) {
            addRandomQuote()
        } else {
            _currentQuoteIndex.value = _currentQuoteIndex.value + 1
            _currentQuote.value = quotesList[_currentQuoteIndex.value]
        }
    }

    fun showPrevQuote() {
        _currentQuoteIndex.value = _currentQuoteIndex.value - 1
        _currentQuote.value = quotesList[_currentQuoteIndex.value]
    }

    private fun isLastQuote(index: Int): Boolean {
        return quotesList.last() == quotesList[index]
    }
}