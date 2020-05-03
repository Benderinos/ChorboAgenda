package es.littledavity.dynamicfeatures.create.name

import android.os.Bundle
import android.view.View
import es.littledavity.chorboagenda.ChorboagendaApp
import es.littledavity.commons.ui.base.BaseFragment
import es.littledavity.dynamicfeatures.create.R
import es.littledavity.dynamicfeatures.create.databinding.FragmentNameBinding
import es.littledavity.dynamicfeatures.create.name.di.DaggerNameComponent
import es.littledavity.dynamicfeatures.create.name.di.NameModule

/**
 * Chorbo name view containing bottom navigation bar with different chorbos tabs.
 *
 * @see BaseFragment
 */
class NameFragment : BaseFragment<FragmentNameBinding, NameViewModel>(
    layoutId = R.layout.fragment_name
) {

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

    }

    /**
     * Initialize dagger injection dependency graph.
     */
    override fun onInitDependencyInjection() {
        DaggerNameComponent
            .builder()
            .coreComponent(ChorboagendaApp.coreComponent(requireContext()))
            .nameModule(NameModule(this))
            .build()
            .inject(this)
    }

    /**
     * Initialize view data binding variables.
     */
    override fun onInitDataBinding() {
        viewBinding.viewModel = viewModel
    }

    override fun onClear() {
    }
}