export class CookieCaller {
    static getCookieValue(key: string) {
        let cookie: string = document.cookie
        let rawPairs: string[] = cookie.split(';')
        for (let i = 0; i < rawPairs.length; i++) {
            if (rawPairs[i].match(key)) {
                return rawPairs[i].split('=')[1]
            }
        }
    }

    static setCookieValue(key: string, value: string) {
        document.cookie = key + '=' + value
    }
}