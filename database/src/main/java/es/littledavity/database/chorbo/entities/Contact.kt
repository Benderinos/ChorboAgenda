/*
 * Copyright 2021 dev.id
 */
package es.littledavity.database.chorbo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity represents when a user adds a Chorbo, containing the different
 * info required for display on screen.
 */
@Entity(
    tableName = Contact.Schema.TABLE_NAME
)
internal data class Contact(
    @ColumnInfo(name = Schema.ID) @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: Image?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = Schema.ARTWORKS) val artworks: List<Image>,
    @ColumnInfo(name = Schema.SCREENSHOTS) val screenshots: List<Image>,
    @ColumnInfo(name = Schema.CREATE_TIMESTAMP) val createTimestamp: Long,
    @ColumnInfo(name = Schema.AGE) val age: String,
    @ColumnInfo(name = Schema.RATING) val rating: String?,
    @ColumnInfo(name = Schema.CREATION_DATE) val creationDate: CreationDate,
    @ColumnInfo(name = Schema.COUNTRY) val country: String
) {

    object Schema {
        const val TABLE_NAME = "chorbo"
        const val ID = "id"
        const val ARTWORKS = "artworks"
        const val SCREENSHOTS = "screenshots"
        const val CREATE_TIMESTAMP = "create_timestamp"
        const val AGE = "age"
        const val RATING = "rating"
        const val CREATION_DATE = "creation_date"
        const val COUNTRY = "country"
    }
}
