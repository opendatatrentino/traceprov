package eu.trentorise.opendata.traceprov.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.trentorise.opendata.commons.validation.ValidationError;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import eu.trentorise.opendata.commons.internal.joda.time.DateTime;

/**
 * A View represents an immutable local snapshot of a corresponding object in a
 * foreign server. The view can also represent objects to be created on the
 * foreign server.
 *
 * Views are immutable, and each view should implement a builder() method.
 *
 * Must implement equals and hashcode.
 *
 * View must have no read/fetch/write methods, such methods must stay in a
 * Service
 *
 * View data need not to be correct, in such case validation errors can be
 * retrieved.
 *
 * Superceeds {@link eu.trentorise.opendata.opendatarise.ekb.View}
 *
 * @author David Leoni
 */
@Immutable
public interface View<T> extends Storable {

    /**
     * Returns the odr internal id of the stored view, which must be uniquely
     * distinguish the view among all views among the same category. There can
     * be many views having the same {@link #getUrl()}, but they all must have
     * different id.
     */
    @JsonProperty("@id")
    @Override
    long getId();

    /**
     * Returns the id of the environment that generated the controlled object
     * (i.e. a website, an organization...)
     */
    public long getOriginId();

    /**
     * Returns the url of the object controlled by the view (that is, the
     * external id). For views of new objects, the URL might not correspond to a
     * real URL on the server of origin.
     */
    public String getUrl();

    /**
     * Converts the view to the format understandable by the origin/target
     * server.
     */
    T toForeignFormat();

    /**
     * Returns true if this view is newer of the provided view. Comparison is
     * done only on the timestamp or version, *independently* from the view
     * status. Both views URLs must match, otherwise an IllegalArgumentException
     * is thrown.
     */
    boolean newerThan(View<T> ctr);

    /**
     * Returns an immutable map containing field names of the view and eventual
     * corresponding errors.
     */
    Map<String, ValidationError> getErrors();

    /**
     * Returns the timestamp when the view was created on local TraceProv database.
     */
    DateTime getTimestamp();

    /**
     * Returns true if the controlled object is equal to the controlled object
     * in the provided view, regardless of the objects linked by it. Thus for
     * this equality relation view properties ( like i.e. {@link #getId()},
     * {@link #getTimestamp()}, ...) must not be taken into consideration.
     *
     * @see #deepEquals(eu.trentorise.opendata.opendatarise.db.View)
     */
    boolean shallowEquals(View<T> view);

    /**
     * Returns true if the controlled object and all the objects linked by it
     * are equal to the controlled object in the provided view. Thus for this
     * equality relation view properties ( like i.e. {@link #getId()},
     * {@link #getTimestamp()}, ...) must not be taken into consideration.
     *
     * @see #shallowEquals(eu.trentorise.opendata.opendatarise.db.View)
     */
    boolean deepEquals(View<T> view);

    /**
     * Returns a new immutable view with given id set.
     */
    View<T> withId(long id);

    /**
     * Returns a list of views from which this view derives from. For example,
     * it can be made with the old view and this view dependencies. TODO
     * probably we need to keep this description a bit vague, as exactly
     * expressing provenance may be too complex (§i.e. think about merges)
     */
    List<Long> derivesFrom();
}
