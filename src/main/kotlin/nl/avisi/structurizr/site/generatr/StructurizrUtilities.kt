package nl.avisi.structurizr.site.generatr

import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

val Model.includedSoftwareSystems: List<SoftwareSystem>
    get() = softwareSystems.filter { it.includedSoftwareSystem }

val SoftwareSystem.includedSoftwareSystem
    get () = this.location != Location.External
