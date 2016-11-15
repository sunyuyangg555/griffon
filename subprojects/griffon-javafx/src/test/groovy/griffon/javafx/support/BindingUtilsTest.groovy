/*
 * Copyright 2008-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.javafx.support

import javafx.application.Platform
import javafx.beans.binding.StringBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet
import javafx.embed.swing.JFXPanel
import org.junit.Test

import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.function.Function

import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

class BindingUtilsTest {
    static {
        // initialize UI toolkit
        new JFXPanel()
    }

    @Test
    void "Join list with string delimiter"() {
        ObservableList<Object> items = FXCollections.observableArrayList()
        StringBinding joined = BindingUtils.join(items, ', ')

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('A'))

        items << 1
        assertThat(joined.get(), equalTo('A, 1'))

        items << Runnable
        assertThat(joined.get(), equalTo('A, 1, interface java.lang.Runnable'))
    }

    @Test
    void "Join list with observable delimiter"() {
        ObservableList<Object> items = FXCollections.observableArrayList()
        StringProperty delimiter = new SimpleStringProperty(', ')
        StringBinding joined = BindingUtils.join(items, delimiter)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('A'))

        items << 1
        assertThat(joined.get(), equalTo('A, 1'))

        items << Runnable
        assertThat(joined.get(), equalTo('A, 1, interface java.lang.Runnable'))

        delimiter.set(':')
        assertThat(joined.get(), equalTo('A:1:interface java.lang.Runnable'))
    }

    @Test
    void "Join list with string delimiter and mapper"() {
        ObservableList<Object> items = FXCollections.observableArrayList()
        Function<String, String> mapper = { s -> '"' + s + '"'}
        StringBinding joined = BindingUtils.join(items, ', ', mapper)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('"A"'))

        items << 1
        assertThat(joined.get(), equalTo('"A", "1"'))

        items << Runnable
        assertThat(joined.get(), equalTo('"A", "1", "interface java.lang.Runnable"'))
    }

    @Test
    void "Join list with observable delimiter and mapper"() {
        ObservableList<Object> items = FXCollections.observableArrayList()
        StringProperty delimiter = new SimpleStringProperty(', ')
        Function<String, String> function = { s -> '"' + s + '"'}
        ObjectProperty<Function<String, String>> mapper = new SimpleObjectProperty(function)
        StringBinding joined = BindingUtils.join(items, delimiter, mapper)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('"A"'))

        items << 1
        assertThat(joined.get(), equalTo('"A", "1"'))

        items << Runnable
        assertThat(joined.get(), equalTo('"A", "1", "interface java.lang.Runnable"'))

        delimiter.set(':')
        assertThat(joined.get(), equalTo('"A":"1":"interface java.lang.Runnable"'))

        mapper.set({ s -> '[' + s + ']'} as Function)
        assertThat(joined.get(), equalTo('[A]:[1]:[interface java.lang.Runnable]'))
    }

    @Test
    void "Join set with string delimiter"() {
        ObservableSet<Object> items = FXCollections.observableSet()
        StringBinding joined = BindingUtils.join(items, ', ')

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('A'))

        items << 1
        assertThat(joined.get(), equalTo('A, 1'))

        items << Runnable
        assertThat(joined.get(), equalTo('A, 1, interface java.lang.Runnable'))
    }

    @Test
    void "Join set with observable delimiter"() {
        ObservableSet<Object> items = FXCollections.observableSet()
        StringProperty delimiter = new SimpleStringProperty(', ')
        StringBinding joined = BindingUtils.join(items, delimiter)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('A'))

        items << 1
        assertThat(joined.get(), equalTo('A, 1'))

        items << Runnable
        assertThat(joined.get(), equalTo('A, 1, interface java.lang.Runnable'))

        delimiter.set(':')
        assertThat(joined.get(), equalTo('A:1:interface java.lang.Runnable'))
    }


    @Test
    void "Join set with string delimiter and mapper"() {
        ObservableSet<Object> items = FXCollections.observableSet()
        Function<String, String> mapper = { s -> '"' + s + '"'}
        StringBinding joined = BindingUtils.join(items, ', ', mapper)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('"A"'))

        items << 1
        assertThat(joined.get(), equalTo('"A", "1"'))

        items << Runnable
        assertThat(joined.get(), equalTo('"A", "1", "interface java.lang.Runnable"'))
    }

    @Test
    void "Join set with observable delimiter and mapper"() {
        ObservableSet<Object> items = FXCollections.observableSet()
        StringProperty delimiter = new SimpleStringProperty(', ')
        Function<String, String> function = { s -> '"' + s + '"'}
        ObjectProperty<Function<String, String>> mapper = new SimpleObjectProperty(function)
        StringBinding joined = BindingUtils.join(items, delimiter, mapper)

        assertThat(joined.get(), equalTo(''))

        items << 'A'
        assertThat(joined.get(), equalTo('"A"'))

        items << 1
        assertThat(joined.get(), equalTo('"A", "1"'))

        items << Runnable
        assertThat(joined.get(), equalTo('"A", "1", "interface java.lang.Runnable"'))

        delimiter.set(':')
        assertThat(joined.get(), equalTo('"A":"1":"interface java.lang.Runnable"'))

        mapper.set({ s -> '[' + s + ']'} as Function)
        assertThat(joined.get(), equalTo('[A]:[1]:[interface java.lang.Runnable]'))
    }

    @Test
    void "Join map with string delimiter"() {
        ObservableMap<Object, Object> items = FXCollections.observableHashMap()
        StringBinding joined = BindingUtils.join(items, '; ')

        assertThat(joined.get(), equalTo(''))

        items.key1 = 'value1'
        assertThat(joined.get(), equalTo('key1=value1'))

        items.key2 = 'value2'
        assertThat(joined.get(), equalTo('key1=value1; key2=value2'))
    }

    @Test
    void "Join map with observable delimiter"() {
        ObservableMap<Object, Object> items = FXCollections.observableHashMap()
        StringProperty delimiter = new SimpleStringProperty('; ')
        StringBinding joined = BindingUtils.join(items, delimiter)

        assertThat(joined.get(), equalTo(''))

        items.key1 = 'value1'
        assertThat(joined.get(), equalTo('key1=value1'))

        items.key2 = 'value2'
        assertThat(joined.get(), equalTo('key1=value1; key2=value2'))

        delimiter.set(', ')
        assertThat(joined.get(), equalTo('key1=value1, key2=value2'))
    }

    @Test
    void "Join map with string delimiter and mapper"() {
        ObservableMap<Object, Object> items = FXCollections.observableHashMap()
        Function<Map.Entry<Object,Object>, String> function = { e -> e.key + ':' + e.value }
        StringBinding joined = BindingUtils.join(items, '; ', function)

        assertThat(joined.get(), equalTo(''))

        items.key1 = 'value1'
        assertThat(joined.get(), equalTo('key1:value1'))

        items.key2 = 'value2'
        assertThat(joined.get(), equalTo('key1:value1; key2:value2'))
    }

    @Test
    void "Join map with observable delimiter and mapper"() {
        ObservableMap<Object, Object> items = FXCollections.observableHashMap()
        StringProperty delimiter = new SimpleStringProperty('; ')
        Function<Map.Entry<Object,Object>, String> function = { e -> e.key + '=' + e.value }
        ObjectProperty<Function<Map.Entry<Object,Object>, String>> mapper = new SimpleObjectProperty<>(function)
        StringBinding joined = BindingUtils.join(items, delimiter, mapper)

        assertThat(joined.get(), equalTo(''))

        items.key1 = 'value1'
        assertThat(joined.get(), equalTo('key1=value1'))

        items.key2 = 'value2'
        assertThat(joined.get(), equalTo('key1=value1; key2=value2'))

        delimiter.set(', ')
        assertThat(joined.get(), equalTo('key1=value1, key2=value2'))

        mapper.set({ e -> e.key + ':' + e.value } as Function)
        assertThat(joined.get(), equalTo('key1:value1, key2:value2'))
    }

    private static <T> T runInsideUISync(Callable<T> callable) {
        if (Platform.isFxApplicationThread()) {
            return callable.call()
        }

        T result = null
        CountDownLatch latch = new CountDownLatch(1)
        Platform.runLater {
            result = callable.call()
            latch.countDown()
        }
        latch.await()
        result
    }
}
