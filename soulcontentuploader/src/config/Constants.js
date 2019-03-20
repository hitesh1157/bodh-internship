export const PRODUCTION_API = ""
export const STAGING_API = ""
const u = path => {
	let current_env_api = STAGING_API
	if(process.env.REACT_APP_API_ENV === "production"){
		current_env_api = PRODUCTION_API
	}
	return current_env_api + path;
}

export const RELIGION = ""
export const PUBLISH_TYPES = ["", "", "", ""]
export const PLAYLIST_TYPES = ["", ""]
export const SECTS = ["", ""]

export const UPLOAD_AUDIO = u("/upload/audio")
export const UPLOAD_THUMBNAIL = u("/upload/thumbnail")
export const COPY_AUDIO_FROM_URL = u("/upload/copyAudioFromUrl")

export const CREATE_SPEAKER = u("/speaker")
export const GET_SPEAKER = u("/speaker/")
export const UPDATE_SPEAKER = u("/speaker/")
export const GET_ALL_SPEAKERS = u("/speakers/" + RELIGION)
export const SPEAKER_TYPES = ["", "", "", "", "", ""]

export const CREATE_LECTURE_WITH_AUDIO = u("/lecture/audio")
export const CREATE_AUDIO_LECTURE = u("/lecture/copyAudioFromUrl")
export const CREATE_VIDEO_LECTURE = u("/lecture/copyVideoFromUrl")
export const GET_LECTURE = u("/lecture/")
export const GET_ALL_LECTURES = u("/lectures/" + RELIGION)
export const GET_ALL_UNASSIGNED_LECTURES = u("/lectures/unassigned/" + RELIGION)
export const UPDATE_LECTURE = u("/lecture/")

export const CREATE_PLAYLIST = u("/playlist")
export const GET_PLAYLIST = u("/playlist/")
export const GET_ALL_PLAYLISTS = u("/playlists/" + RELIGION)
export const UPDATE_PLAYLIST = u("/playlist/")
export const GET_ALL_LECTIRES_ELIGIBLE_FOR_PLAYLIST = u("/lectures/eligibleForPlaylist/")

export const CREATE_CATEGORY = u("/category")
export const GET_CATEGORY = u("/category/")
export const GET_ALL_CATEGORIES = u("/categories/" + RELIGION)
export const UPDATE_CATEGORY = u("/category/")
export const UPDATE_CATEGORY_REORDER = u('/categories/reorder')

export const CREATE_POEM = u("/poem")
export const GET_POEM = u("/poem/")
export const GET_ALL_POEMS = u("/poems/" + RELIGION)
export const UPDATE_POEM = u("/poem/")
export const POEM_TYPES = ["", "", "", "", "", "", "", ""]

export const QUILLPAD = ""
export const QUILLPAD_FE = u("/quill/")
