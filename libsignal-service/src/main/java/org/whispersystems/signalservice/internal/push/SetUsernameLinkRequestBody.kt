package org.whispersystems.signalservice.internal.push

import com.fasterxml.jackson.annotation.JsonProperty

/** Request body for setting a username link on the service. */
data class SetUsernameLinkRequestBody(@JsonProperty("usernameLinkEncryptedValue") val usernameLinkEncryptedValue: String, @JsonProperty("keepLinkHandle") val keepLinkHandle: Boolean)
