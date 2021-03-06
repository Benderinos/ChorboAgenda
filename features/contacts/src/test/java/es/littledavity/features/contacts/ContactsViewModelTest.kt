package es.littledavity.features.contacts

import app.cash.turbine.test
import es.littledavity.commons.ui.base.events.GeneralCommand
import es.littledavity.commons.ui.widgets.contacts.ContactModel
import es.littledavity.commons.ui.widgets.contacts.ContactsUiState
import es.littledavity.domain.DomainContact
import es.littledavity.domain.contacts.usecases.ObserveContactsUseCase
import es.littledavity.testUtils.DOMAIN_CONTACTS
import es.littledavity.testUtils.FakeDispatcherProvider
import es.littledavity.testUtils.FakeErrorMapper
import es.littledavity.testUtils.FakeLogger
import es.littledavity.testUtils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat

class ContactsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var observeContactsUseCase: ObserveContactsUseCase

    private lateinit var logger: FakeLogger
    private lateinit var viewModel: ContactsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        viewModel = ContactsViewModel(
            observeContactsUseCase = observeContactsUseCase,
            uiStateFactory = FakeUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }

    @Test
    fun emitCorrectUiStatesWhenLoadingData() = mainCoroutineRule.runBlockingTest {
        coEvery { observeContactsUseCase.execute(any()) } returns flowOf(DOMAIN_CONTACTS)

        viewModel.uiState.test {
            viewModel.loadData()
            val emptyState = expectItem()
            val loadingState = expectItem()
            val resultState = expectItem()

            assertThat(emptyState is ContactsUiState.Empty).isTrue
            assertThat(loadingState is ContactsUiState.Loading).isTrue
            assertThat(resultState is ContactsUiState.Result).isTrue
            assertThat((resultState as ContactsUiState.Result).items).hasSize(DOMAIN_CONTACTS.size)
        }
    }

    @Test
    fun LogsErrorWhenContactsLoadingFails() = mainCoroutineRule.runBlockingTest {
        coEvery { observeContactsUseCase.execute(any()) } returns flow { throw Exception("error") }
        viewModel.loadData()
        assertThat(logger.errorMessage).isNotEmpty
    }

    @Test
    fun DispatchesToastShowingCommandWhenContactsLoadingFails() = mainCoroutineRule.runBlockingTest {
        coEvery { observeContactsUseCase.execute(any()) } returns flow { throw Exception("Error") }
        viewModel.commandFlow.test {
            viewModel.loadData()
            assertThat(expectItem() is GeneralCommand.ShowLongToast).isTrue
        }
    }

    @Test
    fun RouteToInoScreenWhenContactIsClicked() = mainCoroutineRule.runBlockingTest {
        val contactModel = ContactModel(
            id = 1,
            name = "name",
            image = "image",
            phone = "1231412"
        )
        viewModel.routeFlow.test {
            viewModel.onContactClicked(contactModel)
            val route = expectItem()
            assertThat(route is ContactsRoute.Info).isTrue
            assertThat((route as ContactsRoute.Info).contactId).isEqualTo(contactModel.id)
        }
    }

    private class FakeUiStateFactory : ContactsUiStateFactory {

        override fun createWithEmptyState(): ContactsUiState {
            return ContactsUiState.Empty(iconId = -1, title = "title")
        }

        override fun createWithLoadingState(): ContactsUiState {
            return ContactsUiState.Loading
        }

        override fun createWithResultState(contacts: List<DomainContact>): ContactsUiState {
            return ContactsUiState.Result(
                contacts.map {
                    ContactModel(
                        id = it.id,
                        name = it.name,
                        image = it.image,
                        phone = it.phone
                    )
                }
            )
        }
    }
}