class SchemeParser {
    static String getScheme(String url) throws Exception {
        int idx = url.indexOf(':');
        if (idx == -1) throw new Exception("invalid url: can't locate ':' symbol");
        return url.substring(0, idx);
    }
}
