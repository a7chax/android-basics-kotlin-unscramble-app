package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

  private lateinit var currentWord : String
  private var wordlist : MutableList<String> = mutableListOf()



  private var _score = MutableLiveData(0)
  val score : LiveData<Int>
    get() = _score

  private var _currentWordCount = MutableLiveData(0)
  val currentWordCount : LiveData<Int>
    get() = _currentWordCount

  private val _currentScrambledWord =  MutableLiveData<String>()
  val currentScrambledWord : LiveData<Spannable> = Transformations.map(_currentScrambledWord){
    if(it == null){
      SpannableString("")
    }else{
      val scrambledWord = it.toString()
      val spannable : Spannable = SpannableString(scrambledWord)
      spannable.setSpan(
        TtsSpan.VerbatimBuilder(scrambledWord).build(),
        0,
        scrambledWord.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
      )
      spannable
    }
  }
//    get() = _currentScrambledWord


  private fun getNextWord(){
    Log.d("Type current score", _score.value!!::class.java.typeName.toString())

    currentWord = allWordsList.random()
    val tempWord =currentWord.toCharArray()
    tempWord.shuffle()

    while (String(tempWord).equals(currentWord,false)){
      tempWord.shuffle()
    }

    if (wordlist.contains(currentWord)){
      getNextWord()
    }else{
      _currentScrambledWord.value = String(tempWord)
      _currentWordCount.value = (_currentWordCount.value)?.inc()
      wordlist.add(currentWord)
    }
  }



  init {
    Log.d("GameFragment", "GameViewModel created!")
    getNextWord()
  }



  fun nextWord() : Boolean {
    return if (currentWordCount.value!! < MAX_NO_OF_WORDS){
      getNextWord()
      true
    }else{
      false
    }
  }

    private fun increaseScore(){
    _score.value = (_score.value)?.plus(SCORE_INCREASE)
  }

  fun isUserWordCorrect(playerWord : String) : Boolean{
    if(playerWord.equals(currentWord, true)){
      increaseScore()
      return true
    }
    return  false
  }

  fun reinitializeData(){
    _score.value = 0
    _currentWordCount.value = 0
    wordlist.clear()
    getNextWord()
  }


}