import {FRONT_API_PREFIX} from "@api/common/commonUrl";
import {axiosOptions, axiosRequest, commonContentType} from "@api/service";

const baseUrl = FRONT_API_PREFIX + 'media/'

/**
 * 根据条件获取期刊的聚类信息
 * @param param
 * @returns {Promise<unknown>}
 */
export function getMediaFacet(param) {
  return axiosRequest(baseUrl + 'getMediaFacet', param, axiosOptions(null, commonContentType.json))
}

/**
 * 根据条件获取期刊分页信息
 * @param param
 * @returns {Promise<unknown>}
 */
export function getMediaPage(param) {
  return axiosRequest(baseUrl + 'getMediaPage', param, axiosOptions(null, commonContentType.json))
}
