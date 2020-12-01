/**
 *
 */
package de.concretecows.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.Test;

/**
 * Copyright 2020 heimburgerj
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author heimburgerj
 *
 */
public class OutputStreamConversionTest {

    /**
     * Test method for
     * {@link de.concretecows.common.utils.OutputStreamConversion#convert(de.concretecows.common.utils.OutputStreamConversion.ThrowingConsumer, de.concretecows.common.utils.OutputStreamConversion.ThrowingFunction)}.
     */
    @Test
    public void testConvert() throws Exception {
        TestType object = new TestType();
        object.setName("TestName");
        object.setAge(12);
        object.setValue("123");
        OutputStreamConversion outputStreamConversion = new OutputStreamConversion();
        TestType result = outputStreamConversion.convert(os -> JaxbUtil.marshalToStream(object, TestType.class, os),
                is -> JaxbUtil.unmarshalFromStream(TestType.class, is));

        assertEquals(object, result);
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    public static class TestType {
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + age;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TestType other = (TestType) obj;
            if (age != other.age)
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        private String name;
        private int age;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
