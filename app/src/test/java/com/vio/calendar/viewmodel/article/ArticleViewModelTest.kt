package com.vio.calendar.viewmodel.article

import com.vio.calendar.data.article.ArticleRepository
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArticleViewModelTest {
    private lateinit var articleViewModel: ArticleViewModel

    @Mock
    lateinit var repository: ArticleRepository

    @Before
    fun setup() {
        articleViewModel = ArticleViewModel(repository)
    }


}