export const PRODUCTION_API = "https://api.bodh.me"
export const STAGING_API = "https://stage-api.bodh.me"
const u = path => {
	let current_env_api = STAGING_API
	if(process.env.REACT_APP_API_ENV === "production"){
		current_env_api = PRODUCTION_API
	}
	return current_env_api + path;
}

export const RELIGION = "JAINISM"
export const PUBLISH_TYPES = ["UNPUBLISHED_UNVERIFIED", "UNPUBLISHED_VERIFIED", "PUBLISHED_UNVERIFIED", "PUBLISHED_VERIFIED"]
export const PLAYLIST_TYPES = ["ACADEMIC", "GENERIC"]
export const SECTS = ["DIGAMBAR", "SVETAMBAR"]

export const UPLOAD_AUDIO = u("/upload/audio")
export const UPLOAD_THUMBNAIL = u("/upload/thumbnail")
export const COPY_AUDIO_FROM_URL = u("/upload/copyAudioFromUrl")

export const CREATE_SPEAKER = u("/speaker")
export const GET_SPEAKER = u("/speaker/")
export const UPDATE_SPEAKER = u("/speaker/")
export const GET_ALL_SPEAKERS = u("/speakers/" + RELIGION)
export const SPEAKER_TYPES = ["ACHARYA", "MAHARAJ", "PANDIT", "KSHULLAK", "MATA", "BRAMHACHARI"]

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
export const POEM_TYPES = ["AARTI", "BHAJAN", "STROTRA", "BHAKTI", "POOJA", "PAATH", "CHALISA", "STUTI"]

export const QUILLPAD = "http://xlit.quillpad.in/quillpad_backend2/processWordJSON"
export const QUILLPAD_FE = u("/quill/")
