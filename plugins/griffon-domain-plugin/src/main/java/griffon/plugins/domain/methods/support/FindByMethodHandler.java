/*
 * Copyright 2008-2014 the original author or authors.
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
package griffon.plugins.domain.methods.support;

import griffon.plugins.domain.GriffonDomain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Andres Almiray
 */
public interface FindByMethodHandler {
    @Nullable
    <T extends GriffonDomain> T findBy(@Nonnull Class<T> clazz, @Nonnull String clause, @Nonnull Object[] arguments);

    @Nullable
    <T extends GriffonDomain> T findBy(@Nonnull Class<T> clazz, @Nonnull String clause, @Nonnull List<Object> arguments);
}