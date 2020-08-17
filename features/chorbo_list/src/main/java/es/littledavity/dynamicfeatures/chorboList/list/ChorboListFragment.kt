/*
 * Copyright 2020 littledavity
 */
package es.littledavity.dynamicfeatures.chorboList.list

import android.os.Bundle
import android.view.View
import androidx.paging.PagedList
import com.airbnb.lottie.LottieDrawable
import es.littledavity.chorboagenda.ChorboagendaApp
import es.littledavity.commons.ui.base.BaseFragment
import es.littledavity.commons.ui.extensions.gridLayoutManager
import es.littledavity.commons.ui.extensions.observe
import es.littledavity.dynamicfeatures.chorboList.list.adapter.ChorboListAdapterState
import es.littledavity.dynamicfeatures.chorboList.list.adapter.ChorbosListAdapter
import es.littledavity.dynamicfeatures.chorboList.list.di.ChorboListModule
import es.littledavity.dynamicfeatures.chorboList.list.di.DaggerChorboListComponent
import es.littledavity.dynamicfeatures.chorboList.list.model.ChorboItem
import es.littledavity.dynamicfeatures.chorbo_list.R
import es.littledavity.dynamicfeatures.chorbo_list.databinding.FragmentChorboListBinding
import javax.inject.Inject

/**
 * Chorbo list principal view containing bottom navigation bar with different chorbos tabs.
 *
 * @see BaseFragment
 */
class ChorboListFragment : BaseFragment<FragmentChorboListBinding, ChorboListViewModel>(
    layoutId = R.layout.fragment_chorbo_list
) {
    companion object {
        private const val PERCENT_100 = 100
        private const val PERCENT_ANIM = 25
    }

    @Inject
    lateinit var viewAdapter: ChorbosListAdapter

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param view The view returned by onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @see BaseFragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.state, ::onViewStateChange)
        observe(viewModel.viewState, ::onViewStateChange)
        observe(viewModel.data, ::onViewDataChange)
        observe(viewModel.event, ::onViewEvent)
    }

    /**
     * Initialize dagger injection dependency graph.
     */
    override fun onInitDependencyInjection() {
        DaggerChorboListComponent
            .builder()
            .coreComponent(ChorboagendaApp.coreComponent(requireContext()))
            .chorboListModule(ChorboListModule(this))
            .build()
            .inject(this)
    }

    /**
     * Initialize view data binding variables.
     */
    override fun onInitDataBinding() {
        viewBinding.viewModel = viewModel
        viewBinding.includeList.chorboList.apply {
            adapter = viewAdapter
            gridLayoutManager?.spanSizeLookup = viewAdapter.getSpanSizeLookup()
        }
    }

    override fun onClear() = Unit

    // ============================================================================================
    //  Private observers methods
    // ============================================================================================

    /**
     * Observer view data change on [ChorboListViewModel].
     *
     * @param viewData Paged list of chorbos.
     */
    private fun onViewDataChange(viewData: PagedList<ChorboItem>) {
        viewAdapter.submitList(viewData)
    }

    /**
     * Observer view state change on [ChorboListViewModel].
     *
     * @param viewState State of chorbo list.
     */
    private fun onViewStateChange(viewState: ChorboListViewState) {
        when (viewState) {
            is ChorboListViewState.Loaded ->
                viewAdapter.submitState(ChorboListAdapterState.Added)
            is ChorboListViewState.AddLoading ->
                viewAdapter.submitState(ChorboListAdapterState.AddLoading)
            is ChorboListViewState.AddError ->
                viewAdapter.submitState(ChorboListAdapterState.AddError)
            is ChorboListViewState.NoMoreElements ->
                viewAdapter.submitState(ChorboListAdapterState.NoMore)
            is ChorboListViewState.Empty ->
                viewBinding.includeListEmpty.openOptionsFab.repeatCount = LottieDrawable.INFINITE
            is ChorboListViewState.SelectElement -> {
                viewAdapter.selectItem(viewState.position)
                if (viewState.last) {
                    viewModel.changeViewState(ChorboListViewState.NoMoreElements)
                }
            }
        }
    }

    /**
     * Observer view event change on [ChorboListViewModel].
     *
     * @param viewEvent Event on chorbos list.
     */
    private fun onViewEvent(viewEvent: ChorboListViewEvent) {
        when (viewEvent) {
            is ChorboListViewEvent.OpenChorboOptions -> playAnimationAndNavigate()
        }
    }

    private fun playAnimationAndNavigate() {
        viewBinding.includeListEmpty.openOptionsFab.cancelAnimation()
        viewBinding.includeListEmpty.openOptionsFab.repeatCount = 0
        viewBinding.includeListEmpty.openOptionsFab.addAnimatorUpdateListener {
            val progress = (it.animatedValue as Float * PERCENT_100).toInt()
            if (progress >= PERCENT_ANIM) {
                val direction = ChorboListFragmentDirections.toName()
                viewModel.navigate(direction)
            }
        }
        viewBinding.includeListEmpty.openOptionsFab.playAnimation()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLoadedChorboList()
    }
}
