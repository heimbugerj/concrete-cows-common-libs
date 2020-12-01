/**
 *
 */
package de.concretecows.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class OutputStreamConversion {

    Logger LOGGER = LogManager.getLogger(OutputStreamConversion.class);

    private final ExecutorService es = Executors.newSingleThreadExecutor();

    public <T> T convert(final ThrowingConsumer<OutputStream, Exception> source,
            ThrowingFunction<InputStream, T, Exception> sinkFunction) throws ConversionException {
        PipedOutputStream pos = new PipedOutputStream();//we need to close the output stream before we can get the result
        try (PipedInputStream pis = new PipedInputStream();) {
            pis.connect(pos);
            Future<T> future = this.es.submit(new Callable<T>() {

                @Override
                public T call() throws Exception {
                    LOGGER.info("Reading input stream ...");
                    T result = sinkFunction.apply(pis);
                    LOGGER.info("Read input stream.");
                    return result;
                }
            });
            LOGGER.info("Writing output stream ...");
            source.accept(pos);
            LOGGER.info("Written output stream.");
            pos.close();
            return future.get();
        } catch (Exception e) {
            throw new ConversionException("Error while converting output stream to input stream!", e);
        } finally {
            try {
                pos.close();
            } catch (Exception e) {
                LOGGER.error("Failed to close pos.");
            }
        }
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    /**
     * This exception indicates that an error occurred while converting.
     *
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
    public static class ConversionException extends Exception {
        private static final long serialVersionUID = 1L;

        public ConversionException(String message, Throwable cause) {
            super(message, cause);
        }

        public ConversionException(String message) {
            super(message);
        }

    }

}
