/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.szhem.osgi.util;

import org.junit.Test;
import org.osgi.framework.Bundle;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BundleDelegatingClassLoaderTest {

    @Test
    public void testCreate() throws Exception {
        Bundle bundle = mock(Bundle.class);
        new BundleDelegatingClassLoader(bundle, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullBundle() throws Exception {
        new BundleDelegatingClassLoader(null, null);
    }

    @Test
    public void testFindClass() throws Exception {
        Bundle bundle = mock(Bundle.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, null);
        classLoader.findClass(getClass().getName());

        verify(bundle).loadClass(eq(getClass().getName()));
    }

    @Test(expected = ClassNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testFindClassNoClass() throws Exception {
        Bundle bundle = mock(Bundle.class);
        when(bundle.loadClass(anyString())).thenThrow(ClassNotFoundException.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, null);
        classLoader.findClass(getClass().getName());
    }

    @Test
    public void testFindResource() throws Exception {
        String resource = getClass().getName().replace('.', '/') + ".class";

        Bundle bundle = mock(Bundle.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, getClass().getClassLoader());
        assertThat(classLoader.findResource(resource), notNullValue());
        
        verify(bundle).getResource(eq(resource));
    }

    @Test
    public void testFindResources() throws Exception {
        String resource = getClass().getName().replace('.', '/') + ".class";

        Bundle bundle = mock(Bundle.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, getClass().getClassLoader());
        assertThat(classLoader.findResources(resource).hasMoreElements(), equalTo(true));

        verify(bundle).getResources(eq(resource));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLoadClass() throws Exception {
        Bundle bundle = mock(Bundle.class);
        when(bundle.loadClass(anyString())).thenThrow(ClassNotFoundException.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, getClass().getClassLoader());
        assertThat(classLoader.loadClass(getClass().getName(), false), notNullValue());

        verify(bundle).loadClass(eq(getClass().getName()));
    }

    @Test(expected = ClassNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testLoadClassNoClass() throws Exception {
        Bundle bundle = mock(Bundle.class);
        when(bundle.loadClass("NotFoundClass")).thenThrow(ClassNotFoundException.class);

        BundleDelegatingClassLoader classLoader = new BundleDelegatingClassLoader(bundle, getClass().getClassLoader());
        classLoader.loadClass("NotFoundClass", false);
    }

}
