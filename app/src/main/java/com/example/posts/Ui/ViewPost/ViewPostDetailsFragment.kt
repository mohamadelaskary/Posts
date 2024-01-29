package com.example.posts.Ui.ViewPost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.exampel.posts.Model.Status
import com.example.posts.Tools.LoadingDialog
import com.example.posts.Ui.Posts.PostsFragment.Companion.POST_ID_KEY
import com.example.posts.databinding.FragmentViewPostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPostDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ViewPostDetailsFragment()
    }

    private lateinit var viewModel: ViewPostDetailsViewModel
    private lateinit var binding: FragmentViewPostDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPostDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewPostDetailsViewModel::class.java]
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null){
            val postId = requireArguments().getInt(POST_ID_KEY)
            viewModel.getPost(postId)
        }
        observeGettingPostData()
    }

    private fun observeGettingPostData() {
        viewModel.getPostStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                    loadingDialog.show()
                    hideData()
                }
                Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    showData()
                }
                else -> {
                    loadingDialog.dismiss()
                    showError(it.message)
                }
            }
        }
        viewModel.getPostLiveData.observe(requireActivity()){
            binding.postTitle.text = it.title
            binding.postBody.text = it.body
        }
    }

    private fun showError(message:String) {
        binding.postTitle.visibility = GONE
        binding.postBody.visibility = VISIBLE
        binding.line.visibility = GONE
        binding.postBody.text = message
    }

    private fun showData() {
        binding.postTitle.visibility = VISIBLE
        binding.postBody.visibility = VISIBLE
        binding.line.visibility = VISIBLE
    }

    private fun hideData() {
        binding.postTitle.visibility = GONE
        binding.postBody.visibility = GONE
        binding.line.visibility = GONE
    }
}