package com.github.fge.msgsimple.provider;

import com.github.fge.msgsimple.source.MessageSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class StaticMessageSourceProviderTest
{
    private MessageSource source;
    private MessageSource source2;

    private StaticMessageSourceProvider.Builder builder;

    @BeforeMethod
    public void init()
    {
        source = mock(MessageSource.class);
        source2 = mock(MessageSource.class);
        builder = StaticMessageSourceProvider.newBuilder();
    }

    @Test
    public void cannotSetNullDefaultSource()
    {
        try {
            builder.setDefaultSource(null);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "cannot set null default source");
        }
    }

    @Test
    public void cannotAddSourceForNullLocale()
    {
        try {
            builder.addSource(null, source);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "null keys are not allowed");
        }
    }

    @Test
    public void cannotAddNullSourceForLocale()
    {
        try {
            builder.addSource(Locale.ROOT, null);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "null sources are not allowed");
        }
    }

    @Test
    public void defaultSourceIsProvidedIfNoOtherSourceIsPresent()
    {
        builder.setDefaultSource(source);
        assertSame(builder.build().getMessageSource(Locale.ROOT), source);
    }

    @Test(dependsOnMethods = "defaultSourceIsProvidedIfNoOtherSourceIsPresent")
    public void localeSpecificSourceOverridesDefaultSource()
    {
        final Locale locale = Locale.CHINA;
        builder.setDefaultSource(source).addSource(locale, source2);
        assertSame(builder.build().getMessageSource(locale), source2);
        assertSame(builder.build().getMessageSource(Locale.ROOT), source);
    }

    @Test(dependsOnMethods = "localeSpecificSourceOverridesDefaultSource")
    public void localeSpecificSourceIsLatestAdded()
    {
        final Locale locale = Locale.CHINA;
        builder.addSource(locale, source).addSource(locale, source2);
        assertSame(builder.build().getMessageSource(locale), source2);
    }
}