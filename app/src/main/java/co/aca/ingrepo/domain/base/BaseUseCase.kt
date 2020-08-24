package co.aca.ingrepo.domain.base

import androidx.annotation.CallSuper
import co.aca.ingrepo.R
import co.aca.ingrepo.repository.base.BaseRequest
import co.aca.ingrepo.repository.base.BaseResponse
import co.aca.ingrepo.repository.base.ErrorModel
import co.aca.ingrepo.util.Strings
import co.aca.ingrepo.util.UseCaseListener
import co.aca.ingrepo.util.alertdialog.DialogActionListener
import co.aca.ingrepo.util.alertdialog.DialogContent
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseUseCase<Response, Params> {

    private val REQUEST_TAG = this.javaClass.simpleName

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var strings: Strings

    private var listener: ((Response?, ErrorModel?) -> Unit)? = null
    private var onDialogListener: ((DialogContent) -> Unit)? = null
    private var onToastListener: ((String) -> Unit)? = null
    var useCaseListener: UseCaseListener? = null

    private val compositeDisposable = CompositeDisposable()

    abstract fun execute(params: Params)

    open fun isLoadingEnable(): Boolean {
        return true
    }

    /**
     * Servis cagrisinde servisi belirtmek icin kullanilir
     */
    @CallSuper
    fun execute(params: Params, listener: ((Response?, ErrorModel?) -> Unit)? = null) {
        this.listener = listener

        execute(params)
    }

    /**
     * Lifecycle tamamlandığında silinecek disposable'lar tutulur
     *
     * @param disposable disposable
     */
    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


    /**
     * Memory leak önlenmesi için disposable'lar silinir.
     */
    @CallSuper
    open fun clear() {
        compositeDisposable.clear()
    }

    fun <Request : BaseRequest> sendRequest(request: Request, task: (Request) -> Single<Response>) {
        if (isLoadingEnable()) {
            useCaseListener?.addToQueue(REQUEST_TAG)
        }

        add(
            task(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getResponseListener())
        )
    }

    fun sendRequest(task: () -> Single<Response>) {
        if (isLoadingEnable()) {
            useCaseListener?.addToQueue(REQUEST_TAG)
        }

        add(
            task()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getResponseListener())
        )
    }

    @CallSuper
    open fun onSendError(errorModel: ErrorModel) {
        listener?.invoke(null, errorModel)

        useCaseListener?.removeFromQueue(REQUEST_TAG)
    }

    @CallSuper
    open fun onSendSuccess(t: Response) {
        listener?.invoke(t, null)

        useCaseListener?.removeFromQueue(REQUEST_TAG)
    }

    private fun getResponseListener(): DisposableSingleObserver<Response> {
        return object : DisposableSingleObserver<Response>() {
            override fun onSuccess(t: Response) {
                var res = t as? BaseResponse

                if (res?.errors.isNullOrEmpty()){
                    onSendSuccess(t)
                }else{
                    var dialog = DialogContent()
                    dialog.contentText = res?.errors?.get(0)!!
                    dialog.possitiveButton = R.string.ok
                    dialog.positiveListener = object : DialogActionListener {
                        override fun onClicked() {
                            onSendError(ErrorModel(res?.errors?.get(0)!!))
                        }
                    }
                }
            }

            @CallSuper
            override fun onError(e: Throwable) {
                try {
                    var errorModel: ErrorModel = if (e is UnknownHostException) {
                        ErrorModel(strings.getString(R.string.no_internet_connection))
                    } else {
                        gson.fromJson(
                            (e as HttpException).response().errorBody()?.string(),
                            ErrorModel::class.java
                        )
                    }

                    onToastListener?.invoke(errorModel.message)
                    onSendError(ErrorModel(errorModel.message))
                } catch (ex: Exception) {
                    onToastListener?.invoke(e.message!!)
                    onSendError(ErrorModel(e.message!!))
                }
            }
        }
    }

    fun setDialogListener(onDialogListener: (DialogContent) -> Unit) {
        this.onDialogListener = onDialogListener
    }

    fun setToastListener(onToastListener: (String) -> Unit) {
        this.onToastListener = onToastListener
    }
}