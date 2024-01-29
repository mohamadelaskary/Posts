package com.example.posts.Ui.Posts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.exampel.posts.Model.Status
import com.example.posts.R
import com.example.posts.Tools.LoadingDialog
import com.example.posts.Ui.Posts.PostsAdapter
import com.example.posts.Ui.ViewPost.ViewPostDetailsViewModel
import com.example.posts.databinding.FragmentPostsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment(), PostsAdapter.OnPostItemClicked {

    companion object {
        fun newInstance() = PostsFragment()
        val POST_ID_KEY: String = "postIdKey"
    }


    private lateinit var viewModel: PostsViewModel
    private lateinit var binding: FragmentPostsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater,container,false)
        return binding.root
    }
    private lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PostsViewModel::class.java]
        loadingDialog = LoadingDialog(requireContext())
    }
    private lateinit var navigationController :NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PostsViewModel", "getPosts: ")
        viewModel.getPosts()
        navigationController = Navigation.findNavController(requireView())
        setUpPostsRecyclerView()
        observeGettingPosts()
    }

    private fun observeGettingPosts() {
        viewModel.getPostsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                    loadingDialog.show()
                    binding.noPosts.visibility = GONE
                    binding.posts.visibility = GONE
                }
                Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    binding.noPosts.visibility = GONE
                    binding.posts.visibility = VISIBLE
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.noPosts.visibility = VISIBLE
                    binding.noPosts.text = it.message
                    binding.posts.visibility = GONE
                }
            }
        }

        viewModel.getPostsLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                binding.noPosts.visibility = GONE
                binding.posts.visibility = VISIBLE
                postsAdapter.posts = it
            } else {
                binding.noPosts.visibility = VISIBLE
                binding.noPosts.text = getString(R.string.no_posts_found)
                binding.posts.visibility = GONE
            }
        }
    }

    private lateinit var postsAdapter : PostsAdapter
    private fun setUpPostsRecyclerView() {
        postsAdapter = PostsAdapter(this)
        binding.posts.adapter = postsAdapter
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPostClicked(id: Int) {
        val bundle = Bundle()
        bundle.putInt(POST_ID_KEY,id)
        navigationController.navigate(R.id.action_postsFragment_to_viewPostDetailsFragment,bundle)
    }
}