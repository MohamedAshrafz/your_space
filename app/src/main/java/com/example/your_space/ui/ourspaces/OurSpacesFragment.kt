package com.example.your_space.ui.ourspaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.FragmentOurSpacesBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OurSpacesFragment : Fragment() {

    private var _binding: FragmentOurSpacesBinding? = null
    private val binding
        get() = _binding!!

    private val spaceAppViewModel by activityViewModels<OurSpacesViewModel>()
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOurSpacesBinding.inflate(inflater, container, false)

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
                        OurSpacesFragmentDirections.actionSecondFragmentToSpaceDetailsFragment(
                            selectedSpaceItem
                        )
                    )
                spaceAppViewModel.clearSelectedItem()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            spaceAppViewModel.refreshOnSwipe()
//            spaceAppViewModel.refreshOnSwipeNoPagination()
        }

        spaceAppViewModel.isSwipeRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                adaptor.notifyDataSetChanged()
//                binding.spaceItemsRecyclerView.scrollToPosition(0)
            }
        }

        binding.searchView.setOnQueryTextListener(MySearchQueryListener(spaceAppViewModel))
        binding.searchView.setOnSearchClickListener {
            binding.checkBox2.visibility = View.GONE
        }
        binding.searchView.setOnCloseListener {
            binding.checkBox2.visibility = View.VISIBLE
            spaceAppViewModel._switchKey.value = 0
            false
        }

        binding.checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                spaceAppViewModel._switchKey.value = 1
            } else {
                spaceAppViewModel._switchKey.value = 0
            }
        }

        spaceAppViewModel.switchKey.observe(viewLifecycleOwner){
            if (spaceAppViewModel.switchKey.value == 2) {
                binding.checkBox2.visibility = View.GONE
            } else{
                binding.checkBox2.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    class MySearchQueryListener(
        private val spacesViewModel: OurSpacesViewModel,
    ) : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty()){
                spacesViewModel._searchText.value = "%$query%"
                spacesViewModel._switchKey.value = 2
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrEmpty()) {
                spacesViewModel._searchText.value = "%$newText%"
                spacesViewModel._switchKey.value = 2
            }
            return true
        }

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

//            if (!loading && (indexOfLastItem + 1 >= currentItemsCount)) {
//                loading = true
//                onLoadMoreItems()
//            }
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