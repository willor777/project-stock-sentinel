package com.willor.lib_data.domain.usecases

class UseCases(
    val getUserPreferencesUsecase: GetUserPreferencesUsecase,
    val saveUserPreferencesUsecase: SaveUserPreferencesUsecase,
    val getMajorFuturesUsecase: GetMajorFuturesUsecase,
    val getMajorIndicesUsecase: GetMajorIndicesUsecase,
    val getPopularWatchlistOptionsUsecase: GetPopularWatchlistOptionsUsecase,
    val getPopularWatchlistUsecase: GetPopularWatchlistUsecase,
    val getStockQuoteUsecase: GetStockQuoteUsecase,
    val getEtfQuoteUsecase: GetEtfQuoteUsecase,
    val getOptionsOverviewUsecase: GetOptionsOverviewUsecase,
    val getStockCompetitorsUsecase: GetStockCompetitorsUsecase,
    val getSnrLevelsUsecase: GetSnrLevelsUsecase,
    val getUoaUsecase: GetUoaUsecase,
    val getTriggersUsecase: GetTriggersUsecase,
    val loginUsecase: LoginUsecase,
    val registerUsecase: RegisterUsecase
)