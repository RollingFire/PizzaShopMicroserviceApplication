/**
 * Contains static methods that can be used to manage cookie values.
 */
export class CookieCaller {
    static getCookieValue(key: string) {
        let cookie: string = document.cookie
        let rawPairs: string[] = cookie.split(';')
        for (let i = 0; i < rawPairs.length; i++) {
            if (rawPairs[i].match(key)) {
                return rawPairs[i].split('=')[1]
            }
        }
        return null
    }

    static setCookieValue(key: string, value: string) {
        document.cookie = key + '=' + value
    }

    static expireCookieValue(key: string) {
        document.cookie = key + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"' 
    }
}