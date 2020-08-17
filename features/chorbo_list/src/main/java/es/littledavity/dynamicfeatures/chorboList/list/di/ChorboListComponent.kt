/*
 * Copyright 2020 littledavity
 */
package es.littledavity.dynamicfeatures.chorboList.list.di

import dagger.Component
import es.littledavity.core.di.CoreComponent
import es.littledavity.core.di.scopes.FeatureScope
import es.littledavity.dynamicfeatures.chorboList.list.ChorboListFragment

/**
 * Class for which a fully-formed, dependency-injected implementation is to
 * be generated from [ChorboListModule].
 *
 * @see Component
 */
@FeatureScope
@Component(
    modules = [ChorboListModule::class],
    dependencies = [CoreComponent::class]
)
interface ChorboListComponent {

    /**
     * Inject dependencies on component.
     *
     * @param chorboListFragment Chorbo list component.
     */
    fun inject(chorboListFragment: ChorboListFragment)
}
