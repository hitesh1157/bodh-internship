package common.settings;

import play.filters.cors.CORSFilter;
import play.filters.gzip.GzipFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject
    public Filters(CORSFilter corsFilter, GzipFilter gzipFilter, RoutedLoggingFilter routedLoggingFilter) {
        super(corsFilter, gzipFilter, routedLoggingFilter);
    }
}