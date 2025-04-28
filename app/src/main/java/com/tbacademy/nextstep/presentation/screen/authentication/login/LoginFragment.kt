package com.tbacademy.nextstep.presentation.screen.authentication.login

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.DialogForgotBinding
import com.tbacademy.nextstep.databinding.FragmentLoginBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.authentication.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.authentication.login.event.LoginEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val loginViewModel: LoginViewModel by viewModels()


    override fun start() {}

    override fun listeners() {
        setLogInBtnListener()
        setRegisterBtnListener()
        setRememberMeCheckboxListener()
        setInputListeners()
    }



    override fun observers() {
        observeState()
        observeUiState()
        observeEffect()

        setForgotPasswordListener()
    }

    private fun setForgotPasswordListener() {
        binding.btnResetPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun showForgotPasswordDialog() {
        val dialogBinding = DialogForgotBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnReset.setOnClickListener {
            val email = dialogBinding.etEmailForgot.text.toString().trim()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
                dialog.dismiss()
            } else {
                dialogBinding.tlEmail.error = "Email cannot be empty"
            }
        }

        dialog.show()
    }

    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(requireView(), "Reset email sent. Check your inbox!", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), "Failed to send reset email.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }



    private fun observeState() {
        collect(flow = loginViewModel.state) { state ->
            binding.apply {
                loaderLogin.loaderContainer.isVisible = state.isLoading
                overlayBlocker.isVisible = state.isLoading

                tlEmail.error = state.emailErrorMessage?.let { getString(it) }
                tlPassword.error = state.passwordErrorMessage?.let { getString(it) }

                btnLogin.isEnabled = state.isLogInEnabled
            }
        }
    }

    private fun observeUiState() {
        collect(flow = loginViewModel.uiState) { state ->
            binding.apply {
                cbRememberMe.isChecked = state.rememberMe
            }
        }
    }

    private fun setLogInBtnListener() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.Submit)
        }
    }

    private fun setRegisterBtnListener() {
        binding.btnRegister.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.RegisterButtonClicked)
        }
    }


    private fun setRememberMeCheckboxListener() {
        binding.cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            loginViewModel.onEvent(LoginEvent.RememberMeChanged(isChecked))
        }
    }

    private fun setInputListeners() {
        setEmailInputListener()
        setPasswordInputListener()
    }

    private fun setEmailInputListener() {
        binding.etEmail.onTextChanged { email ->
            loginViewModel.onEvent(LoginEvent.EmailChanged(email = email))
        }
    }

    private fun setPasswordInputListener() {
        binding.etPassword.onTextChanged { password ->
            loginViewModel.onEvent(LoginEvent.PasswordChanged(password = password))
        }
    }

    private fun observeEffect() {
        collectLatest(loginViewModel.effects) {
            when (it) {
                LoginEffect.NavToMainFragment -> navToMainFragment()
                LoginEffect.NavToRegisterFragment -> navToRegisterFragment()
                is LoginEffect.ShowError -> showMessage(it.message)
            }
        }
    }

    private fun navToRegisterFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        )
    }

    private fun navToMainFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToMainFragment()
        )
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}