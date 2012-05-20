## OSGi Utilities

1. [What is it?](#what-is-it)
2. [How to use?](#how-to-use)
3. [License](#license)

### What is it?
This project contains rather useful OSGi utilities to deal with OSGi services.

Currently there are the following features:

* [DSL for OSGi filters](#osgi-services-filter-dsl)
* [Collections API to track for OSGi services](#collections-api-to-track-for-osgi-services)

### How to use?

#### OSGi Services Filter DSL

Here is a simple example to make things clear:

    import static com.github.szhem.osgi.util.filter.Filters.*;

    ...

    Filter filter = and(
        eq("a", "b"),
        ne("b", "d"),
        approx("g", "s"),
        le("y", "z"),
        raw("(&(g=n)(f=n))"),
        like("f", "g", LikeCriterion.MatchMode.ANYWHERE),
        not(anyEq(attrs))
    ).filter();

The example above will produce the following filter string `(&(a=b)(!(b=d))(g~=s)(y<=z)(&(g=n)(f=n))(f=*g*)(!(|(a=b)(c=d))))`

#### Collections API to track for OSGi services

To start traking for OSGi services you have to do the following steps:

    // 1. get BundleContext somehow
    BundleContext bundleContext = ...;

    // 2. create filter somehow, for example, using Filters DSL
    String serviceFilter = null;

    // 3. optionally specify the ClassLoader to fallback to load exported OSGi service interfaces
    ClassLoader fallbackClassLoader = getClass().getClassLoader();

    // 4. initialize proxy creator to proxy obtained OSGi services as the may come and go
    OsgiProxyCreator proxyCreator = new OsgiDefaultProxyCreator();

    // 5. create OsgiServiceList or OsgiServiceCollection
    List<Service> services = new OsgiServiceList<Service>(bundleContext, serviceFilter, fallbackClassLoader, proxyCreator);

    // 6. start tracking for services
    services.startTracking();

    // 7. now list with services is updated dynamically even during iterating through it, as services will come and go
    for (Service service : services) {
        service.doSomething();
    }

    // 8. finally you have to release all resources obtained and unregister all the listeners from the OSGi framework
    services.stopTracking();

### License

The component is distributed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
