package com.timetable.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/timetableResource")
public class TimeTableResource
{

	@POST
	@Path("/save/class")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveClassDetails(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.saveClassDetails(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	@POST
	@Path("/save/teacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveTeacherDetails(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.saveTeacherDetails(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	@POST
	@Path("/save/classteacher")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveClassTeacherDetails(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.saveClassTeacherDetails(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	@POST
	@Path("/save/preference")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response savePreference(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.savePreference(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	@POST
	@Path("/generate/timetable")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateTimeTable(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.generateTimeTable(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	@POST
	@Path("/get/details")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllClassTeacherDetails(@Context HttpServletRequest request, JSONObject jsonObject) {
		try {
			TimeTableResourceHelper timeTableResourceHelper = new TimeTableResourceHelper();
			JSONObject responseJson = timeTableResourceHelper.getAllClassTeacherDetails(request, jsonObject);
			return buildPostResponse(responseJson);
		} catch (Exception e) {
			return buildErrorReponse(e);
		}

	}

	public static Response buildResponse(Status status, Object entity, String mediaType) {
		return Response.status(status).entity(entity).type(mediaType).header("X-Content-Type-Options", "nosniff").build();
	}

	public static Response buildResponse(Status status, JSONObject entity) {
		return buildResponse(status, entity, MediaType.APPLICATION_JSON);
	}

	public static Response buildResponse(Status status, JSONArray entity) {
		return buildResponse(status, entity, MediaType.APPLICATION_JSON);
	}

	public static Response buildGetResponse(JSONObject entity) {
		return buildResponse(Status.OK, entity);
	}

	public static Response buildGetResponse(JSONArray entity) {
		return buildResponse(Status.OK, entity);
	}

	public static Response buildPostResponse(JSONObject entity) {
		return buildResponse(Status.CREATED, entity);
	}

	public static Response buildErrorReponse(Throwable t) {
		JSONObject json = new JSONObject();
		try {
			json.put("error", t.getMessage());
		} catch (JSONException e) {
		}
		return buildResponse(Status.INTERNAL_SERVER_ERROR, json);
	}
}
