package sample.ui.test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import sample.R
import sample.databinding.ActivityTestBinding
import sample.ui.base.BaseActivity

class TestActivity :
    BaseActivity<ActivityTestBinding, TestViewModel>() {

    override fun viewModelClass() = TestViewModel::class.java
    override fun layoutId() = R.layout.activity_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bt.setOnClickListener {
            binding.fv.validate {
                Log.d("testText", "Text is : ${binding.et.text.toString()}")
                Toast.makeText(this, binding.et.text.toString(), Toast.LENGTH_LONG).show()
            }
        }

        binding.et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.et.text!!.length > binding.et.counterMaxLength)
                    binding.et.error = "Text is longer than allowed limit"
                else
                    binding.et.isErrorEnabled = false
            }
        })

    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }
}