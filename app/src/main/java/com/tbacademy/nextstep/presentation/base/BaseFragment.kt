package com.tbacademy.nextstep.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> (
    private val inflater : (LayoutInflater, ViewGroup?, Boolean) -> VB
): Fragment(
){

    private var _binding : VB? = null
    protected val binding get() = _binding!!

    abstract fun start()
    abstract fun listeners()
    abstract fun observers()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
        start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}