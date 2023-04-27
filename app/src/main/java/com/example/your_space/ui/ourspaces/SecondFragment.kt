package com.example.your_space.ui.ourspaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding
        get() = _binding!!

    private val spaceAppViewModel by activityViewModels<OurSpacesViewModel>()
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.viewModel = spaceAppViewModel
        val adaptor =
            OurSpacesAdabter { spaceItem ->
                spaceAppViewModel.onSelectSpaceItem(spaceItem)
            }

        val recyclerViewLayoutManager = LinearLayoutManager(this.context)

        val recyclerViewScrollListener = EndlessScrollListener(recyclerViewLayoutManager) {
            spaceAppViewModel.loadMoreWorkingSpaces()
        }

        binding.spaceItemsRecyclerView.addOnScrollListener(recyclerViewScrollListener)
        binding.spaceItemsRecyclerView.layoutManager = recyclerViewLayoutManager

        spaceAppViewModel.isPageLoading.observe(viewLifecycleOwner) { isLoading ->

//                binding.spaceItemsRecyclerView.adapter?.notifyDataSetChanged()
            recyclerViewScrollListener.setLoading(isLoading)
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.spaceItemsRecyclerView.adapter = adaptor

        spaceAppViewModel.selectedSpaceItem.observe(viewLifecycleOwner) { selectedSpaceItem ->
            if (selectedSpaceItem != null) {
                requireView().findNavController()
                    .navigate(
                        SecondFragmentDirections.actionSecondFragmentToSpaceDetailsFragment(
                            selectedSpaceItem
                        )
                    )
                spaceAppViewModel.clearSelectedItem()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adaptor.notifyDataSetChanged()
            spaceAppViewModel.refreshOnSwipe()
        }

        spaceAppViewModel.isSwipeRefreshing.observe(viewLifecycleOwner){ isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
        }

        return binding.root
    }

    class EndlessScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val onLoadMoreItems: () -> Unit
    ) : RecyclerView.OnScrollListener() {
        private var loading = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val currentItemsCount = layoutManager.itemCount
            val indexOfLastItem = layoutManager.findLastVisibleItemPosition()

            if (!loading && (indexOfLastItem + 1 >= currentItemsCount)) {
                loading = true
                onLoadMoreItems()
            }
        }

        fun setLoading(loading: Boolean) {
            this.loading = loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}