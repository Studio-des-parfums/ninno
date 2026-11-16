package com.ninno.app.network

/** Adresse du backend Ninno (Railway). Seul endroit à modifier si elle change. */
private const val PROD_BASE_URL = "https://ninno-back-production.up.railway.app"

/** Adresse de remplacement propre à la plateforme (backend local en dev web), sinon null. */
internal expect fun localApiUrlOverride(): String?

internal val BASE_URL: String = localApiUrlOverride() ?: PROD_BASE_URL
