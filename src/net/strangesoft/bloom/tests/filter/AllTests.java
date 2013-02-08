package net.strangesoft.bloom.tests.filter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FullBloomFilterTest.class, CountingBloomFilterTest.class })
public class AllTests {

}
