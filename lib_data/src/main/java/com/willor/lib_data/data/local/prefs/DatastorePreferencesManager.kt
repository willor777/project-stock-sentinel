package com.willor.lib_data.data.local.prefs


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.willor.lib_data.data.local.prefs.DatastorePreferencesManager.Companion.DATASTORE_NAME
import com.willor.lib_data.domain.dataobjs.DataResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.datastore by preferencesDataStore(
    name = DATASTORE_NAME
)

@Suppress("unused")
class DatastorePreferencesManager(
    private val context: Context
) {

    fun getUserPrefs(): Flow<DataResourceState<UserPreferences>> {
        return context.datastore.data.map{


            val usrPrefsJson: String? = it[USER_PREFERENCES_KEY]

            if (usrPrefsJson == null){
                DataResourceState.Success(data = UserPreferences())
            }else{
                DataResourceState.Success(data = UserPreferences.toUserPreferences(usrPrefsJson))
            }
        }
    }

    suspend fun saveUserPrefs(userPrefs: UserPreferences){
        context.datastore.edit {
            val json = UserPreferences.toJson(userPrefs)
            it[USER_PREFERENCES_KEY] = json
        }
    }

    companion object{
        val USER_PREFERENCES_KEY = stringPreferencesKey("userprefs")
        const val DATASTORE_NAME = "user-preferences-datastore"
    }
}