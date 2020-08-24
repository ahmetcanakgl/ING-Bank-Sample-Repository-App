package co.aca.ingrepo.util

import co.aca.ingrepo.util.alertdialog.DialogContent

interface UseCaseListener {

    fun showDialog(dgContent: DialogContent)

    fun addToQueue(tag: String)

    fun removeFromQueue(tag: String)

    fun timeoutExpired(message: String)
}