package nz.ac.canterbury.seng440.mwa172.locationalarm

import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings
import kotlin.reflect.KProperty

class GoNapState {
    var activeAlarm: Alarm? by Synchronize(null)
    var settings: Settings by Synchronize(Settings.DefaultSettings)
}

/**
 * From StackOverflow: [Rishav Chudal](https://stackoverflow.com/questions/45445991/synchronize-property-getters-setters)
 */
private class Synchronize<T>(defaultValue: T) {
    private var backingField = defaultValue

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return synchronized(this) {
            backingField
        }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        synchronized(this) {
            backingField = value
        }
    }
}