package com.mohcoder.demoapp.ui.wizard

/**
 * Copyright (c) 2024 MohLovesCode
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mohcoder.demoapp.R
import com.mohcoder.demoapp.databinding.FragmentWizardPageBinding
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.mohcoder.demoapp.utils.AppPreferences

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class WizardPageFragment : BaseFragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IS_LAST_PAGE = "is_last_page"

        fun newInstance(title: String, description: String, isLastPage: Boolean = false): WizardPageFragment {
            val fragment = WizardPageFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putBoolean(ARG_IS_LAST_PAGE, isLastPage)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentWizardPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var title: String
    private lateinit var description: String
    private var isLatePage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE, "")
            description = it.getString(ARG_DESCRIPTION, "")
            isLatePage = it.getBoolean(ARG_IS_LAST_PAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWizardPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTextView.text = title
        binding.descriptionTextView.text = description
        binding.doneButton.visibility = if (isLatePage) View.VISIBLE else View.GONE
        binding.doneButton.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()
            AppPreferences(requireContext()).setWizardCompleted()
            findNavController().navigate(R.id.loginFragment, null, navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}