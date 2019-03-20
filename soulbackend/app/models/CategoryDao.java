package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import common.settings.StartUpHandler;
import common.utils.Constants;
import common.utils.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import play.libs.Json;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harshit on 11/03/17.
 */
public class CategoryDao extends BasicDAO<Category, Object> {
    private static volatile CategoryDao mInstance;
    private Datastore datastore = getDatastore();

    private CategoryDao() {
        super(StartUpHandler.getMorphiaObject().getMongo(), StartUpHandler.getMorphiaObject().getMorphia(), StartUpHandler.getMorphiaObject().getDatastore().getDB().getName());
    }

    public static CategoryDao getInstance() {
        if (mInstance == null) {
            synchronized (CategoryDao.class) {
                if (mInstance == null)
                    mInstance = new CategoryDao();
            }
        }
        return mInstance;
    }

    public Response insert(Category category) {
        Category duplicateCategory = datastore.find(Category.class).field("religion").equal(category.getReligion()).field("name").equal(category.getName()).get();
        if(duplicateCategory == null) {
            datastore.save(category);
            ObjectNode objectNode = Json.newObject();
            objectNode.put(Constants.ID, String.valueOf(category.id));
            return Response.successResponse(objectNode);
        } else {
            return Response.errorResponse("Duplicate Entry");
        }
    }

    public Response getCategory(String id) {
        Category category = datastore.get(Category.class, new ObjectId(id));
        if (category != null)
            return Response.successResponse(category);
        else
            return Response.errorResponse("Not found");
    }

    public Response getAllCategories() {
        List<Category> categories = datastore.find(Category.class).order("position").asList();
        return Response.successResponse(categories);
    }

    public Response getAllCategories(String religion) {
        List<Category> categories = datastore.find(Category.class).field("religion").equal(Religion.fromString(religion)).order("position").asList();
        return Response.successResponse(categories);
    }

    public Response getAllPublishedCategories(String religion) {
        List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
        List<Category> categories = datastore.find(Category.class).field("religion").equal(Religion.fromString(religion)).field("publishType").in(publishTypeList).order("position").asList();
        return Response.successResponse(categories);
    }

    public Response getAllPublishedCategories(String religion, String sect) {
        List<PublishType> publishTypeList = Arrays.asList(PublishType.PUBLISHED_UNVERIFIED, PublishType.PUBLISHED_VERIFIED);
        Query<Category> query = datastore.find(Category.class).field("religion").equal(Religion.fromString(religion)).field("publishType").in(publishTypeList);
        if(Sect.fromString(sect) != Sect.BOTH){
            query = query.field("sect").equal(Sect.fromString(sect));
        }
        List<Category> categories = query.order("position").asList();
        return Response.successResponse(categories);
    }

    public Response update(Category category, String id) {
        try {
            category.id = new ObjectId(id);
            Key<Category> key = datastore.merge(category);
            return Response.successResponse(datastore.get(Category.class, category.id));
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

    public Response updateOrder(List<String> ids) {

        try {
            for (int id = 0; id < ids.size(); id++) {
                Category category = datastore.get(Category.class, new ObjectId(ids.get(id)));
                if (category != null) {
                    category.setPosition(id);
                    datastore.save(category);
                }

            }
            return Response.successResponse("Reordering successful.");
        } catch (Exception e) {
            Utils.logException(e);
            return Response.errorResponse(e.getMessage());
        }
    }

    public Response getPlaylists(String id, List<PublishType> publishTypeList) {
        Category category = datastore.get(Category.class, new ObjectId(id));
        List<Playlist> playlists = category.getPlaylists();
        List<Playlist> finalPlaylists = new ArrayList<>();
        for (Playlist playlist : playlists) {
            if (publishTypeList.contains(playlist.getPublishType())) {
                finalPlaylists.add(playlist);
            }
        }

        return Response.successResponse(finalPlaylists);
    }

    public Response add(List<String> playlistIds, String id) {
        Category category = datastore.get(Category.class, new ObjectId(id));
        List<Playlist> playlists = category.getPlaylists();
        List<String> playlistStrings = category.getPlaylistIds();
        for (String playlistId : playlistIds) {
            if (!playlistStrings.contains(playlistId)) {
                Playlist playlist = datastore.get(Playlist.class, new ObjectId(playlistId));
                if (playlist != null)
                    playlists.add(playlist);
            }
        }
        category.setPlaylists(playlists);
        datastore.save(category);
        return Response.successResponse(category);
    }

    public Response remove(List<String> playlistIds, String id) {
        Category category = datastore.get(Category.class, new ObjectId(id));
        List<Playlist> playlists = category.getPlaylists();
        for (String playlistId : playlistIds) {
            Playlist playlist = datastore.get(Playlist.class, new ObjectId(playlistId));
            if (playlist != null) {
                playlists.remove(playlist);
            }
        }
        category.setPlaylists(playlists);
        datastore.save(category);
        return Response.successResponse(category);
    }

}
