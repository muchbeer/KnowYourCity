package raum.muchbeer.knowyourcity.util

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import raum.muchbeer.knowyourcity.data.EventSubject

object EventBus {
    private val liveData = MutableLiveData<Event>()

    fun post(subject: EventSubject, event: Any?) {
        this.liveData.postValue(Event(subject, event))
    }

    fun getLiveData(subject: EventSubject): MutableLiveData<Event> {
        val mediatorLiveData = MediatorLiveData<Event>()
        mediatorLiveData.addSource(liveData) {
            if (it.subject == subject) {
                mediatorLiveData.value = it
            }
        }

        return mediatorLiveData
    }

    class Event(val subject: EventSubject, private val value: Any?) {
        private var read = false

        fun setRead() {
            read = true
        }

        fun isRead(): Boolean {
            return read
        }

        fun getValue(): Any? {
            return value
        }
    }
}