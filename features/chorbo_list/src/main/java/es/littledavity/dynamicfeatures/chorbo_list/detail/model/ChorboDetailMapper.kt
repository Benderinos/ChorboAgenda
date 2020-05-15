/*
 * Copyright 2020 littledavity
 */
package es.littledavity.dynamicfeatures.chorbo_list.detail.model

import es.littledavity.core.database.chorbo.Chorbo
import es.littledavity.core.mapper.Mapper

class ChorboDetailMapper : Mapper<Chorbo, ChorboDetailItem> {

    override suspend fun map(from: Chorbo) = ChorboDetailItem(
        id = from.id,
        name = from.name,
        image = from.image,
        countryCode = from.countryCode,
        countryName = from.countryName,
        flag = from.flag,
        whatsapp = from.whatsapp,
        instagram = from.instagram
    )

    override suspend fun reverseMap(from: ChorboDetailItem) = Chorbo(
        id = from.id,
        name = from.name,
        image = from.image,
        countryCode = from.countryCode,
        countryName = from.countryName,
        flag = from.flag,
        whatsapp = from.whatsapp,
        instagram = from.instagram
    )
}
