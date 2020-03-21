package sample.ui.test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import meow.controller
import sample.R
import sample.databinding.ActivityTestBinding
import sample.ui.base.BaseActivity

class TestActivity :
    BaseActivity<ActivityTestBinding, TestViewModel>() {

    override fun viewModelClass() = TestViewModel::class.java
    override fun layoutId() = R.layout.activity_test

    override fun onCreate(savedInstanceState: Bundle?) {
        controller.defaultFontName = getString(R.string.font_mainRegular)
        super.onCreate(savedInstanceState)

        bt.setOnClickListener {
            fv.validate()
            Log.d("testText", "Text is : ${et.text.toString()}")
            Toast.makeText(this, et.text.toString(), Toast.LENGTH_LONG).show()
        }

        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et.text!!.length > et.counterMaxLength)
                    et.error = "Text is longer than allowed limit"
                else
                    et.isErrorEnabled = false
            }
        })

    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }
}