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
import com.google.android.material.tabs.TabLayoutMediator
import com.mohcoder.demoapp.R
import com.mohcoder.demoapp.databinding.FragmentWizardBinding
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.mohcoder.demoapp.utils.WizardAdapter

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class WizardFragment : BaseFragment() {

    private var _binding: FragmentWizardBinding? = null
    private val binding get() = _binding!!
    private lateinit var wizardAdapter: WizardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWizardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wizardAdapter = WizardAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        wizardAdapter.apply {
            addFragment(WizardPageFragment.newInstance(resources.getString(R.string.first_page_title), resources.getString(R.string.first_page_description)))
            addFragment(WizardPageFragment.newInstance(resources.getString(R.string.second_page_title), resources.getString(R.string.second_page_description)))
            addFragment(WizardPageFragment.newInstance(resources.getString(R.string.third_page_title), resources.getString(R.string.third_page_description), true))
        }

        binding.viewPager.adapter = wizardAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}