// Copyright 2012 Google Inc. All rights reserved.
(function () {

    var data = {
        "resource": {
            "version": "1",

            "macros": [{
                "function": "__e"
            }, {
                "function": "__cid"
            }],
            "tags": [{
                "function": "__rep",
                "once_per_event": true,
                "vtp_containerId": ["macro", 1],
                "tag_id": 1
            }],
            "predicates": [{
                "function": "_eq",
                "arg0": ["macro", 0],
                "arg1": "gtm.js"
            }],
            "rules": [
                [["if", 0], ["add", 0]]]
        },
        "runtime": []


    };
    /*

     Copyright The Closure Library Authors.
     SPDX-License-Identifier: Apache-2.0
    */
    var aa, ba = function (a) {
        var b = 0;
        return function () {
            return b < a.length ? {done: !1, value: a[b++]} : {done: !0}
        }
    }, ca = function (a) {
        var b = "undefined" != typeof Symbol && Symbol.iterator && a[Symbol.iterator];
        return b ? b.call(a) : {next: ba(a)}
    }, da = "function" == typeof Object.create ? Object.create : function (a) {
        var b = function () {
        };
        b.prototype = a;
        return new b
    }, fa;
    if ("function" == typeof Object.setPrototypeOf) fa = Object.setPrototypeOf; else {
        var ha;
        a:{
            var ka = {a: !0}, na = {};
            try {
                na.__proto__ = ka;
                ha = na.a;
                break a
            } catch (a) {
            }
            ha = !1
        }
        fa = ha ? function (a, b) {
            a.__proto__ = b;
            if (a.__proto__ !== b) throw new TypeError(a + " is not extensible");
            return a
        } : null
    }
    var oa = fa, pa = function (a, b) {
        a.prototype = da(b.prototype);
        a.prototype.constructor = a;
        if (oa) oa(a, b); else for (var c in b) if ("prototype" != c) if (Object.defineProperties) {
            var d = Object.getOwnPropertyDescriptor(b, c);
            d && Object.defineProperty(a, c, d)
        } else a[c] = b[c];
        a.Ti = b.prototype
    }, qa = this || self, ta = function (a) {
        if (a && a != qa) return ra(a.document);
        null === sa && (sa = ra(qa.document));
        return sa
    }, ua = /^[\w+/_-]+[=]{0,2}$/, sa = null, ra = function (a) {
        var b = a.querySelector && a.querySelector("script[nonce]");
        if (b) {
            var c = b.nonce || b.getAttribute("nonce");
            if (c && ua.test(c)) return c
        }
        return ""
    }, va = function (a) {
        return a
    };
    var xa = {}, za = function (a, b) {
        xa[a] = xa[a] || [];
        xa[a][b] = !0
    }, Aa = function (a) {
        for (var b = [], c = xa[a] || [], d = 0; d < c.length; d++) c[d] && (b[Math.floor(d / 6)] ^= 1 << d % 6);
        for (var e = 0; e < b.length; e++) b[e] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(b[e] || 0);
        return b.join("")
    };
    var Ba = function () {
    }, Da = function (a) {
        return "function" == typeof a
    }, g = function (a) {
        return "string" == typeof a
    }, Ga = function (a) {
        return "number" == typeof a && !isNaN(a)
    }, Ha = function (a) {
        var b = "[object Array]" == Object.prototype.toString.call(Object(a));
        Array.isArray ? Array.isArray(a) !== b && za("TAGGING", 4) : za("TAGGING", 5);
        return b
    }, Ia = function (a, b) {
        if (Array.prototype.indexOf) {
            var c = a.indexOf(b);
            return "number" == typeof c ? c : -1
        }
        for (var d = 0; d < a.length; d++) if (a[d] === b) return d;
        return -1
    }, Ja = function (a, b) {
        if (a && Ha(a)) for (var c =
            0; c < a.length; c++) if (a[c] && b(a[c])) return a[c]
    }, Ka = function (a, b) {
        if (!Ga(a) || !Ga(b) || a > b) a = 0, b = 2147483647;
        return Math.floor(Math.random() * (b - a + 1) + a)
    }, Na = function (a, b) {
        for (var c = new La, d = 0; d < a.length; d++) c.set(a[d], !0);
        for (var e = 0; e < b.length; e++) if (c.get(b[e])) return !0;
        return !1
    }, Oa = function (a, b) {
        for (var c in a) Object.prototype.hasOwnProperty.call(a, c) && b(c, a[c])
    }, Qa = function (a) {
        return !!a && ("[object Arguments]" == Object.prototype.toString.call(a) || Object.prototype.hasOwnProperty.call(a, "callee"))
    }, Sa =
        function (a) {
            return Math.round(Number(a)) || 0
        }, Wa = function (a) {
        return "false" == String(a).toLowerCase() ? !1 : !!a
    }, Xa = function (a) {
        var b = [];
        if (Ha(a)) for (var c = 0; c < a.length; c++) b.push(String(a[c]));
        return b
    }, Ya = function (a) {
        return a ? a.replace(/^\s+|\s+$/g, "") : ""
    }, Za = function () {
        return (new Date).getTime()
    }, La = function () {
        this.prefix = "gtm.";
        this.values = {}
    };
    La.prototype.set = function (a, b) {
        this.values[this.prefix + a] = b
    };
    La.prototype.get = function (a) {
        return this.values[this.prefix + a]
    };
    var $a = function (a, b, c) {
        return a && a.hasOwnProperty(b) ? a[b] : c
    }, cb = function (a) {
        var b = a;
        return function () {
            if (b) {
                var c = b;
                b = void 0;
                try {
                    c()
                } catch (d) {
                }
            }
        }
    }, db = function (a, b) {
        for (var c in b) b.hasOwnProperty(c) && (a[c] = b[c])
    }, fb = function (a) {
        for (var b in a) if (a.hasOwnProperty(b)) return !0;
        return !1
    }, hb = function (a, b) {
        for (var c = [], d = 0; d < a.length; d++) c.push(a[d]), c.push.apply(c, b[a[d]] || []);
        return c
    }, ib = function (a, b) {
        for (var c = {}, d = c, e = a.split("."), f = 0; f < e.length - 1; f++) d = d[e[f]] = {};
        d[e[e.length - 1]] = b;
        return c
    }, jb = function (a) {
        var b =
            [];
        Oa(a, function (c, d) {
            10 > c.length && d && b.push(c)
        });
        return b.join(",")
    };/*
 jQuery v1.9.1 (c) 2005, 2012 jQuery Foundation, Inc. jquery.org/license. */
    var kb = /\[object (Boolean|Number|String|Function|Array|Date|RegExp)\]/, lb = function (a) {
        if (null == a) return String(a);
        var b = kb.exec(Object.prototype.toString.call(Object(a)));
        return b ? b[1].toLowerCase() : "object"
    }, nb = function (a, b) {
        return Object.prototype.hasOwnProperty.call(Object(a), b)
    }, ob = function (a) {
        if (!a || "object" != lb(a) || a.nodeType || a == a.window) return !1;
        try {
            if (a.constructor && !nb(a, "constructor") && !nb(a.constructor.prototype, "isPrototypeOf")) return !1
        } catch (c) {
            return !1
        }
        for (var b in a) ;
        return void 0 ===
            b || nb(a, b)
    }, m = function (a, b) {
        var c = b || ("array" == lb(a) ? [] : {}), d;
        for (d in a) if (nb(a, d)) {
            var e = a[d];
            "array" == lb(e) ? ("array" != lb(c[d]) && (c[d] = []), c[d] = m(e, c[d])) : ob(e) ? (ob(c[d]) || (c[d] = {}), c[d] = m(e, c[d])) : c[d] = e
        }
        return c
    };
    var pb = function (a) {
        if (void 0 === a || Ha(a) || ob(a)) return !0;
        switch (typeof a) {
            case "boolean":
            case "number":
            case "string":
            case "function":
                return !0
        }
        return !1
    };
    var qb = function () {
        var a = function (b) {
            return {
                toString: function () {
                    return b
                }
            }
        };
        return {
            Pf: a("consent"),
            Qf: a("consent_always_fire"),
            ce: a("convert_case_to"),
            de: a("convert_false_to"),
            ee: a("convert_null_to"),
            fe: a("convert_true_to"),
            he: a("convert_undefined_to"),
            wi: a("debug_mode_metadata"),
            Na: a("function"),
            Dg: a("instance_name"),
            Fg: a("live_only"),
            Gg: a("malware_disabled"),
            Hg: a("metadata"),
            zi: a("original_activity_id"),
            Ai: a("original_vendor_template_id"),
            Jg: a("once_per_event"),
            Xe: a("once_per_load"),
            af: a("setup_tags"),
            bf: a("tag_id"),
            cf: a("teardown_tags")
        }
    }();
    var Qb;
    var Rb = [], Sb = [], Tb = [], Ub = [], Vb = [], Wb = {}, Xb, Yb, Zb, $b = function (a, b) {
        var c = a["function"];
        if (!c) throw Error("Error: No function name given for function call.");
        var d = Wb[c], e = {}, f;
        for (f in a) if (a.hasOwnProperty(f)) if (0 === f.indexOf("vtp_")) d && b && b.hf && b.hf(a[f]), e[void 0 !== d ? f : f.substr(4)] = a[f]; else if (f === qb.Qf.toString() && a[f]) {
        }
        return void 0 !== d ? d(e) : Qb(c, e, b)
    }, bc = function (a, b, c) {
        c =
            c || [];
        var d = {}, e;
        for (e in a) a.hasOwnProperty(e) && (d[e] = ac(a[e], b, c));
        return d
    }, ac = function (a, b, c) {
        if (Ha(a)) {
            var d;
            switch (a[0]) {
                case "function_id":
                    return a[1];
                case "list":
                    d = [];
                    for (var e = 1; e < a.length; e++) d.push(ac(a[e], b, c));
                    return d;
                case "macro":
                    var f = a[1];
                    if (c[f]) return;
                    var h = Rb[f];
                    if (!h || b.Cd(h)) return;
                    c[f] = !0;
                    try {
                        var k = bc(h, b, c);
                        k.vtp_gtmEventId = b.id;
                        d = $b(k, b);
                        Zb && (d = Zb.jh(d, k))
                    } catch (y) {
                        b.yf && b.yf(y, Number(f)), d = !1
                    }
                    c[f] = !1;
                    return d;
                case "map":
                    d = {};
                    for (var l = 1; l < a.length; l += 2) d[ac(a[l], b, c)] = ac(a[l +
                    1], b, c);
                    return d;
                case "template":
                    d = [];
                    for (var p = !1, q = 1; q < a.length; q++) {
                        var n = ac(a[q], b, c);
                        Yb && (p = p || n === Yb.ic);
                        d.push(n)
                    }
                    return Yb && p ? Yb.mh(d) : d.join("");
                case "escape":
                    d = ac(a[1], b, c);
                    if (Yb && Ha(a[1]) && "macro" === a[1][0] && Yb.Ih(a)) return Yb.Yh(d);
                    d = String(d);
                    for (var u = 2; u < a.length; u++) rb[a[u]] && (d = rb[a[u]](d));
                    return d;
                case "tag":
                    var t = a[1];
                    if (!Ub[t]) throw Error("Unable to resolve tag reference " + t + ".");
                    return d = {qf: a[2], index: t};
                case "zb":
                    var r = {arg0: a[2], arg1: a[3], ignore_case: a[5]};
                    r["function"] = a[1];
                    var v = ec(r, b, c), w = !!a[4];
                    return w || 2 !== v ? w !== (1 === v) : null;
                default:
                    throw Error("Attempting to expand unknown Value type: " + a[0] + ".");
            }
        }
        return a
    }, ec = function (a, b, c) {
        try {
            return Xb(bc(a, b, c))
        } catch (d) {
            JSON.stringify(a)
        }
        return 2
    };
    var fc = null, ic = function (a) {
        function b(n) {
            for (var u = 0; u < n.length; u++) d[n[u]] = !0
        }

        var c = [], d = [];
        fc = gc(a);
        for (var e = 0; e < Sb.length; e++) {
            var f = Sb[e], h = hc(f);
            if (h) {
                for (var k = f.add || [], l = 0; l < k.length; l++) c[k[l]] = !0;
                b(f.block || [])
            } else null === h && b(f.block || [])
        }
        for (var p = [], q = 0; q < Ub.length; q++) c[q] && !d[q] && (p[q] = !0);
        return p
    }, hc = function (a) {
        for (var b = a["if"] || [], c = 0; c < b.length; c++) {
            var d = fc(b[c]);
            if (0 === d) return !1;
            if (2 === d) return null
        }
        for (var e = a.unless || [], f = 0; f < e.length; f++) {
            var h = fc(e[f]);
            if (2 === h) return null;
            if (1 === h) return !1
        }
        return !0
    }, gc = function (a) {
        var b = [];
        return function (c) {
            void 0 === b[c] && (b[c] = ec(Tb[c], a));
            return b[c]
        }
    };
    var jc = {
        jh: function (a, b) {
            b[qb.ce] && "string" === typeof a && (a = 1 == b[qb.ce] ? a.toLowerCase() : a.toUpperCase());
            b.hasOwnProperty(qb.ee) && null === a && (a = b[qb.ee]);
            b.hasOwnProperty(qb.he) && void 0 === a && (a = b[qb.he]);
            b.hasOwnProperty(qb.fe) && !0 === a && (a = b[qb.fe]);
            b.hasOwnProperty(qb.de) && !1 === a && (a = b[qb.de]);
            return a
        }
    };/*
 Copyright (c) 2014 Derek Brans, MIT license https://github.com/krux/postscribe/blob/master/LICENSE. Portions derived from simplehtmlparser, which is licensed under the Apache License, Version 2.0 */
    var C = {
        Ab: "_ee",
        od: "_syn",
        Di: "_uei",
        hd: "_eu",
        Bi: "_pci",
        Wc: "event_callback",
        $b: "event_timeout",
        Z: "gtag.config",
        ya: "gtag.get",
        la: "purchase",
        Ya: "refund",
        Ka: "begin_checkout",
        Wa: "add_to_cart",
        Xa: "remove_from_cart",
        Yf: "view_cart",
        me: "add_to_wishlist",
        xa: "view_item",
        ke: "view_promotion",
        je: "select_promotion",
        Rc: "select_item",
        Xb: "view_item_list",
        ie: "add_payment_info",
        Xf: "add_shipping_info",
        Ba: "value_key",
        Aa: "value_callback",
        ia: "allow_ad_personalization_signals",
        dd: "restricted_data_processing",
        qb: "allow_google_signals",
        ja: "cookie_expires",
        tb: "cookie_update",
        xb: "session_duration",
        na: "user_properties",
        Da: "transport_url",
        M: "ads_data_redaction",
        Le: "user_data",
        ac: "first_party_collection",
        B: "ad_storage",
        J: "analytics_storage",
        ae: "region",
        be: "wait_for_update"
    };
    C.Sc = "page_view", C.ne = "user_engagement", C.Sf = "app_remove", C.Tf = "app_store_refund", C.Uf = "app_store_subscription_cancel", C.Vf = "app_store_subscription_convert", C.Wf = "app_store_subscription_renew", C.Zf = "first_open", C.$f = "first_visit", C.ag = "in_app_purchase", C.bg = "session_start", C.cg = "allow_custom_scripts", C.dg = "allow_display_features", C.Tc = "allow_enhanced_conversions", C.De = "enhanced_conversions", C.Za = "client_id", C.W = "cookie_domain", C.Zb = "cookie_name", C.La = "cookie_path", C.ma = "cookie_flags", C.za = "currency",
        C.xe = "custom_map", C.$c = "groups", C.$a = "language", C.ve = "country", C.xi = "non_interaction", C.vb = "page_location", C.Ca = "page_referrer", C.cd = "page_title", C.wb = "send_page_view", C.Ma = "send_to", C.ed = "session_engaged", C.fc = "session_id", C.fd = "session_number", C.yg = "tracking_id", C.ka = "linker", C.Ea = "url_passthrough", C.ab = "accept_incoming", C.I = "domains", C.eb = "url_position", C.cb = "decorate_forms", C.Ie = "phone_conversion_number", C.Ge = "phone_conversion_callback", C.He = "phone_conversion_css_class", C.Je = "phone_conversion_options",
        C.sg = "phone_conversion_ids", C.rg = "phone_conversion_country_code", C.oe = "aw_remarketing", C.pe = "aw_remarketing_only", C.Yb = "gclid", C.Fa = "value", C.ug = "quantity", C.ig = "affiliation", C.Ce = "tax", C.Be = "shipping", C.Vc = "list_name", C.Ae = "checkout_step", C.ze = "checkout_option", C.jg = "coupon", C.kg = "promotions", C.yb = "transaction_id", C.zb = "user_id", C.vg = "retoken", C.sb = "conversion_linker", C.rb = "conversion_cookie_prefix", C.aa = "cookie_prefix", C.V = "items", C.ue = "aw_merchant_id", C.se = "aw_feed_country", C.te = "aw_feed_language",
        C.qe = "discount", C.ye = "disable_merchant_reported_purchases", C.Fe = "new_customer", C.we = "customer_lifetime_value", C.hg = "dc_natural_search", C.gg = "dc_custom_params", C.zg = "trip_type", C.qg = "passengers", C.og = "method", C.xg = "search_term", C.eg = "content_type", C.pg = "optimize_id", C.lg = "experiments", C.ub = "google_signals", C.Zc = "google_tld", C.hc = "update", C.Yc = "firebase_id", C.bc = "ga_restrict_domain", C.Xc = "event_settings", C.Uc = "dynamic_event_settings", C.wg = "screen_name", C.ng = "_x_19", C.mg = "_x_20", C.bd = "internal_traffic_results",
        C.Ke = "traffic_type", C.cc = "referral_exclusion_definition", C.ad = "ignore_referrer", C.gd = "delivery_postal_code", C.Ee = "estimated_delivery_date", C.fg = "developer_id", C.Ag = [C.ia, C.Tc, C.qb, C.V, C.dd, C.W, C.ja, C.ma, C.Zb, C.La, C.aa, C.tb, C.xe, C.Uc, C.Wc, C.Xc, C.$b, C.ac, C.bc, C.ub, C.Zc, C.$c, C.bd, C.ka, C.cc, C.Ma, C.wb, C.xb, C.Da, C.hc, C.na, C.gd, C.hd],C.Me = [C.vb, C.Ca, C.cd, C.$a, C.wg, C.zb, C.Yc],C.Cg = [C.Sf, C.Tf, C.Uf, C.Vf, C.Wf, C.Zf, C.$f, C.ag, C.bg, C.ne],C.$d = [C.ia, C.Tc, C.oe, C.pe, C.qe, C.se, C.te, C.V, C.ue, C.rb, C.sb, C.W, C.ja, C.ma,
        C.aa, C.za, C.we, C.ye, C.De, C.Ee, C.Yc, C.ac, C.$a, C.Fe, C.vb, C.Ca, C.Ge, C.He, C.Ie, C.Je, C.dd, C.wb, C.Ma, C.gd, C.yb, C.Da, C.hc, C.Ea, C.zb, C.Fa];
    C.Oe = [C.la, C.Ya, C.Ka, C.Wa, C.Xa, C.Yf, C.me, C.xa, C.ke, C.je, C.Xb, C.Rc, C.ie, C.Xf];
    C.Ne = [C.ia, C.qb, C.tb];
    C.Pe = [C.ja, C.$b, C.xb];
    var F = function (a) {
        za("GTM", a)
    };
    var Hc = function (a, b) {
        var c = function () {
        };
        c.prototype = a.prototype;
        var d = new c;
        a.apply(d, Array.prototype.slice.call(arguments, 1));
        return d
    }, Ic = function (a) {
        var b = a;
        return function () {
            if (b) {
                var c = b;
                b = null;
                c()
            }
        }
    };
    var Jc, Mc = function () {
        if (void 0 === Jc) {
            var a = null, b = qa.trustedTypes;
            if (b && b.createPolicy) {
                try {
                    a = b.createPolicy("goog#html", {createHTML: va, createScript: va, createScriptURL: va})
                } catch (c) {
                    qa.console && qa.console.error(c.message)
                }
                Jc = a
            } else Jc = a
        }
        return Jc
    };
    var Oc = function (a, b) {
        this.m = b === Nc ? a : ""
    };
    Oc.prototype.toString = function () {
        return this.m + ""
    };
    var Nc = {};
    var Pc = /^(?:(?:https?|mailto|ftp):|[^:/?#]*(?:[/?#]|$))/i;
    var Qc;
    a:{
        var Rc = qa.navigator;
        if (Rc) {
            var Sc = Rc.userAgent;
            if (Sc) {
                Qc = Sc;
                break a
            }
        }
        Qc = ""
    }
    var Tc = function (a) {
        return -1 != Qc.indexOf(a)
    };
    var Vc = function (a, b, c) {
        this.m = c === Uc ? a : ""
    };
    Vc.prototype.toString = function () {
        return this.m.toString()
    };
    var Wc = function (a) {
        return a instanceof Vc && a.constructor === Vc ? a.m : "type_error:SafeHtml"
    }, Uc = {}, Xc = new Vc(qa.trustedTypes && qa.trustedTypes.emptyHTML || "", 0, Uc);
    var Yc = function (a) {
        var b = !1, c;
        return function () {
            b || (c = a(), b = !0);
            return c
        }
    }(function () {
        var a = document.createElement("div"), b = document.createElement("div");
        b.appendChild(document.createElement("div"));
        a.appendChild(b);
        var c = a.firstChild.firstChild;
        a.innerHTML = Wc(Xc);
        return !c.parentElement
    }), Zc = function (a, b) {
        if (Yc()) for (; a.lastChild;) a.removeChild(a.lastChild);
        a.innerHTML = Wc(b)
    };
    var $c = function (a) {
        var b = Mc(), c = b ? b.createHTML(a) : a;
        return new Vc(c, null, Uc)
    };
    var G = window, H = document, ad = navigator, bd = H.currentScript && H.currentScript.src, cd = function (a, b) {
        var c = G[a];
        G[a] = void 0 === c ? b : c;
        return G[a]
    }, fd = function (a, b) {
        b && (a.addEventListener ? a.onload = b : a.onreadystatechange = function () {
            a.readyState in {loaded: 1, complete: 1} && (a.onreadystatechange = null, b())
        })
    }, gd = function (a, b, c) {
        var d = H.createElement("script");
        d.type = "text/javascript";
        d.async = !0;
        var e, f = Mc(), h = f ? f.createScriptURL(a) : a;
        e = new Oc(h, Nc);
        d.src = e instanceof Oc && e.constructor === Oc ? e.m : "type_error:TrustedResourceUrl";
        var k = ta(d.ownerDocument && d.ownerDocument.defaultView);
        k && d.setAttribute("nonce", k);
        fd(d, b);
        c && (d.onerror = c);
        var l = ta();
        l && d.setAttribute("nonce", l);
        var p = H.getElementsByTagName("script")[0] || H.body || H.head;
        p.parentNode.insertBefore(d, p);
        return d
    }, hd = function () {
        if (bd) {
            var a = bd.toLowerCase();
            if (0 === a.indexOf("https://")) return 2;
            if (0 === a.indexOf("http://")) return 3
        }
        return 1
    }, id = function (a, b) {
        var c = H.createElement("iframe");
        c.height = "0";
        c.width = "0";
        c.style.display = "none";
        c.style.visibility = "hidden";
        var d = H.body && H.body.lastChild || H.body || H.head;
        d.parentNode.insertBefore(c, d);
        fd(c, b);
        void 0 !== a && (c.src = a);
        return c
    }, jd = function (a, b, c) {
        var d = new Image(1, 1);
        d.onload = function () {
            d.onload = null;
            b && b()
        };
        d.onerror = function () {
            d.onerror = null;
            c && c()
        };
        d.src = a;
        return d
    }, kd = function (a, b, c, d) {
        a.addEventListener ? a.addEventListener(b, c, !!d) : a.attachEvent && a.attachEvent("on" + b, c)
    }, ld = function (a, b, c) {
        a.removeEventListener ? a.removeEventListener(b, c, !1) : a.detachEvent && a.detachEvent("on" + b, c)
    }, J = function (a) {
        G.setTimeout(a,
            0)
    }, md = function (a, b) {
        return a && b && a.attributes && a.attributes[b] ? a.attributes[b].value : null
    }, nd = function (a) {
        var b = a.innerText || a.textContent || "";
        b && " " != b && (b = b.replace(/^[\s\xa0]+|[\s\xa0]+$/g, ""));
        b && (b = b.replace(/(\xa0+|\s{2,}|\n|\r\t)/g, " "));
        return b
    }, od = function (a) {
        var b = H.createElement("div"), c = $c("A<div>" + a + "</div>");
        Zc(b, c);
        b = b.lastChild;
        for (var d = []; b.firstChild;) d.push(b.removeChild(b.firstChild));
        return d
    }, pd = function (a, b, c) {
        c = c || 100;
        for (var d = {}, e = 0; e < b.length; e++) d[b[e]] = !0;
        for (var f =
            a, h = 0; f && h <= c; h++) {
            if (d[String(f.tagName).toLowerCase()]) return f;
            f = f.parentElement
        }
        return null
    }, qd = function (a) {
        ad.sendBeacon && ad.sendBeacon(a) || jd(a)
    }, rd = function (a, b) {
        var c = a[b];
        c && "string" === typeof c.animVal && (c = c.animVal);
        return c
    };
    var sd = {}, td = function () {
        return void 0 == sd.gtag_cs_api ? !1 : sd.gtag_cs_api
    };
    var ud = [];

    function vd() {
        var a = cd("google_tag_data", {});
        a.ics || (a.ics = {
            entries: {},
            set: wd,
            update: xd,
            addListener: yd,
            notifyListeners: zd,
            active: !1,
            usedDefault: !1
        });
        return a.ics
    }

    function wd(a, b, c, d, e, f) {
        var h = vd();
        h.active = !0;
        h.usedDefault = !0;
        if (void 0 != b) {
            var k = h.entries, l = k[a] || {}, p = l.region, q = c && g(c) ? c.toUpperCase() : void 0;
            d = d.toUpperCase();
            e = e.toUpperCase();
            if ("" === d || q === e || (q === d ? p !== e : !q && !p)) {
                var n = !!(f && 0 < f && void 0 === l.update),
                    u = {region: q, initial: "granted" === b, update: l.update, quiet: n};
                if ("" !== d || !1 !== l.initial) k[a] = u;
                n && G.setTimeout(function () {
                    k[a] === u && u.quiet && (u.quiet = !1, Ad(a), zd(), za("TAGGING", 2))
                }, f)
            }
        }
    }

    function xd(a, b) {
        var c = vd();
        c.active = !0;
        if (void 0 != b) {
            var d = Bd(a), e = c.entries, f = e[a] = e[a] || {};
            f.update = "granted" === b;
            var h = Bd(a);
            f.quiet ? (f.quiet = !1, Ad(a)) : h !== d && Ad(a)
        }
    }

    function yd(a, b) {
        ud.push({kf: a, vh: b})
    }

    function Ad(a) {
        for (var b = 0; b < ud.length; ++b) {
            var c = ud[b];
            Ha(c.kf) && -1 !== c.kf.indexOf(a) && (c.Cf = !0)
        }
    }

    function zd(a) {
        for (var b = 0; b < ud.length; ++b) {
            var c = ud[b];
            if (c.Cf) {
                c.Cf = !1;
                try {
                    c.vh({jf: a})
                } catch (d) {
                }
            }
        }
    }

    var Bd = function (a) {
        var b = vd().entries[a] || {};
        return void 0 !== b.update ? b.update : void 0 !== b.initial ? b.initial : void 0
    }, Cd = function (a) {
        return (vd().entries[a] || {}).initial
    }, Dd = function (a) {
        return !(vd().entries[a] || {}).quiet
    }, Ed = function () {
        return td() ? vd().active : !1
    }, Fd = function () {
        return vd().usedDefault
    }, Gd = function (a, b) {
        vd().addListener(a, b)
    }, Hd = function (a, b) {
        function c() {
            for (var e = 0; e < b.length; e++) if (!Dd(b[e])) return !0;
            return !1
        }

        if (c()) {
            var d = !1;
            Gd(b, function (e) {
                d || c() || (d = !0, a(e))
            })
        } else a({})
    }, Id = function (a,
                      b) {
        if (!1 === Bd(b)) {
            var c = !1;
            Gd([b], function (d) {
                !c && Bd(b) && (a(d), c = !0)
            })
        }
    };

    function Jd(a) {
        for (var b = [], c = 0; c < Kd.length; c++) {
            var d = a(Kd[c]);
            b[c] = !0 === d ? "1" : !1 === d ? "0" : "-"
        }
        return b.join("")
    }

    var Kd = [C.B, C.J], Ld = function (a) {
        var b = a[C.ae];
        b && F(40);
        var c = a[C.be];
        c && F(41);
        for (var d = Ha(b) ? b : [b], e = 0; e < d.length; ++e) for (var f in a) if (a.hasOwnProperty(f) && f !== C.ae && f !== C.be) if (-1 < Ia(Kd, f)) {
            var h = f, k = a[f], l = d[e];
            vd().set(h, k, l, "CN", "CN-44", c)
        } else {
        }
    }, Md = function (a, b) {
        for (var c in a) if (a.hasOwnProperty(c)) if (-1 <
            Ia(Kd, c)) {
            var d = c, e = a[c];
            vd().update(d, e)
        } else {
        }
        vd().notifyListeners(b)
    }, Nd = function (a) {
        var b = Bd(a);
        return void 0 != b ? b : !0
    }, Od = function () {
        return "G1" + Jd(Bd)
    }, Pd = function (a, b) {
        Hd(a, b)
    };
    var Rd = function (a) {
        return Qd ? H.querySelectorAll(a) : null
    }, Sd = function (a, b) {
        if (!Qd) return null;
        if (Element.prototype.closest) try {
            return a.closest(b)
        } catch (e) {
            return null
        }
        var c = Element.prototype.matches || Element.prototype.webkitMatchesSelector || Element.prototype.mozMatchesSelector || Element.prototype.msMatchesSelector || Element.prototype.oMatchesSelector,
            d = a;
        if (!H.documentElement.contains(d)) return null;
        do {
            try {
                if (c.call(d, b)) return d
            } catch (e) {
                break
            }
            d = d.parentElement || d.parentNode
        } while (null !== d && 1 === d.nodeType);
        return null
    }, Td = !1;
    if (H.querySelectorAll) try {
        var Ud = H.querySelectorAll(":root");
        Ud && 1 == Ud.length && Ud[0] == H.documentElement && (Td = !0)
    } catch (a) {
    }
    var Qd = Td;
    var Vd = function (a) {
        if (H.hidden) return !0;
        var b = a.getBoundingClientRect();
        if (b.top == b.bottom || b.left == b.right || !G.getComputedStyle) return !0;
        var c = G.getComputedStyle(a, null);
        if ("hidden" === c.visibility) return !0;
        for (var d = a, e = c; d;) {
            if ("none" === e.display) return !0;
            var f = e.opacity, h = e.filter;
            if (h) {
                var k = h.indexOf("opacity(");
                0 <= k && (h = h.substring(k + 8, h.indexOf(")", k)), "%" == h.charAt(h.length - 1) && (h = h.substring(0, h.length - 1)), f = Math.min(h, f))
            }
            if (void 0 !== f && 0 >= f) return !0;
            (d = d.parentElement) && (e = G.getComputedStyle(d,
                null))
        }
        return !1
    };
    var ge = /:[0-9]+$/, he = function (a, b, c) {
        for (var d = a.split("&"), e = 0; e < d.length; e++) {
            var f = d[e].split("=");
            if (decodeURIComponent(f[0]).replace(/\+/g, " ") === b) {
                var h = f.slice(1).join("=");
                return c ? h : decodeURIComponent(h).replace(/\+/g, " ")
            }
        }
    }, ke = function (a, b, c, d, e) {
        b && (b = String(b).toLowerCase());
        if ("protocol" === b || "port" === b) a.protocol = ie(a.protocol) || ie(G.location.protocol);
        "port" === b ? a.port = String(Number(a.hostname ? a.port : G.location.port) || ("http" == a.protocol ? 80 : "https" == a.protocol ? 443 : "")) : "host" === b &&
            (a.hostname = (a.hostname || G.location.hostname).replace(ge, "").toLowerCase());
        return je(a, b, c, d, e)
    }, je = function (a, b, c, d, e) {
        var f, h = ie(a.protocol);
        b && (b = String(b).toLowerCase());
        switch (b) {
            case "url_no_fragment":
                f = le(a);
                break;
            case "protocol":
                f = h;
                break;
            case "host":
                f = a.hostname.replace(ge, "").toLowerCase();
                if (c) {
                    var k = /^www\d*\./.exec(f);
                    k && k[0] && (f = f.substr(k[0].length))
                }
                break;
            case "port":
                f = String(Number(a.port) || ("http" == h ? 80 : "https" == h ? 443 : ""));
                break;
            case "path":
                a.pathname || a.hostname || za("TAGGING",
                    1);
                f = "/" == a.pathname.substr(0, 1) ? a.pathname : "/" + a.pathname;
                var l = f.split("/");
                0 <= Ia(d || [], l[l.length - 1]) && (l[l.length - 1] = "");
                f = l.join("/");
                break;
            case "query":
                f = a.search.replace("?", "");
                e && (f = he(f, e, void 0));
                break;
            case "extension":
                var p = a.pathname.split(".");
                f = 1 < p.length ? p[p.length - 1] : "";
                f = f.split("/")[0];
                break;
            case "fragment":
                f = a.hash.replace("#", "");
                break;
            default:
                f = a && a.href
        }
        return f
    }, ie = function (a) {
        return a ? a.replace(":", "").toLowerCase() : ""
    }, le = function (a) {
        var b = "";
        if (a && a.href) {
            var c = a.href.indexOf("#");
            b = 0 > c ? a.href : a.href.substr(0, c)
        }
        return b
    }, me = function (a) {
        var b = H.createElement("a");
        a && (b.href = a);
        var c = b.pathname;
        "/" !== c[0] && (a || za("TAGGING", 1), c = "/" + c);
        var d = b.hostname.replace(ge, "");
        return {
            href: b.href,
            protocol: b.protocol,
            host: b.host,
            hostname: d,
            pathname: c,
            search: b.search,
            hash: b.hash,
            port: b.port
        }
    }, ne = function (a) {
        function b(p) {
            var q = p.split("=")[0];
            return 0 > d.indexOf(q) ? p : q + "=0"
        }

        function c(p) {
            return p.split("&").map(b).filter(function (q) {
                return void 0 != q
            }).join("&")
        }

        var d = "gclid dclid gclaw gcldc gclgp gclha gclgf _gl".split(" "),
            e = me(a), f = a.split(/[?#]/)[0], h = e.search, k = e.hash;
        "?" === h[0] && (h = h.substring(1));
        "#" === k[0] && (k = k.substring(1));
        h = c(h);
        k = c(k);
        "" !== h && (h = "?" + h);
        "" !== k && (k = "#" + k);
        var l = "" + f + h + k;
        "/" === l[l.length - 1] && (l = l.substring(0, l.length - 1));
        return l
    };
    var oe = new RegExp(/[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}/i), pe = new RegExp(/support|noreply/i),
        qe = ["SCRIPT", "IMG", "SVG", "PATH", "BR"], re = ["BR"];

    function se(a) {
        var b;
        if (a === H.body) b = "body"; else {
            var c;
            if (a.id) c = "#" + a.id; else {
                var d;
                if (a.parentElement) {
                    var e;
                    a:{
                        var f = a.parentElement;
                        if (f) {
                            for (var h = 0; h < f.childElementCount; h++) if (f.children[h] === a) {
                                e = h + 1;
                                break a
                            }
                            e = -1
                        } else e = 1
                    }
                    d = se(a.parentElement) + ">:nth-child(" + e + ")"
                } else d = "";
                c = d
            }
            b = c
        }
        return b
    }

    var ve = function () {
        var a = !0;
        var b = a, c;
        var d = [], e = H.body;
        if (e) {
            for (var f = e.querySelectorAll("*"), h = 0; h < f.length && 1E4 > h; h++) {
                var k = f[h];
                if (!(0 <= qe.indexOf(k.tagName.toUpperCase()))) {
                    for (var l = !1, p = 0; p < k.childElementCount && 1E4 > p; p++) if (!(0 <= re.indexOf(k.children[p].tagName.toUpperCase()))) {
                        l = !0;
                        break
                    }
                    l || d.push(k)
                }
            }
            c = {elements: d, status: 1E4 < f.length ? "2" : "1"}
        } else c =
            {elements: d, status: "4"};
        for (var q = c, n = q.elements, u = [], t = 0; t < n.length; t++) {
            var r = n[t], v = r.textContent;
            r.value && (v = r.value);
            if (v) {
                var w = v.match(oe);
                if (w) {
                    var y = w[0], x;
                    if (G.location) {
                        var A = je(G.location, "host", !0);
                        x = 0 <= y.toLowerCase().indexOf(A)
                    } else x = !1;
                    x || u.push({element: r, Pc: y})
                }
            }
        }
        var z;
        for (var B = [], D = 10 < u.length ? "3" : q.status, E = 0; E < u.length &&
        10 > E; E++) {
            var I = u[E].element, M = u[E].Pc, N = !1;
            B.push({Pc: M, querySelector: se(I), tagName: I.tagName, isVisible: !Vd(I), type: 1, Cc: !!N})
        }
        return {elements: B, status: D}
    };
    var Je = {}, L = null, Ke = Math.random();
    Je.D = "UA-144499021-1";
    Je.nc = "2h0";
    Je.yi = "";
    Je.Rf = "ChEIgIT9gQYQhreEyJPDl9bLARInAGMRGj89572BpCVAes6eeCT3J2ZqByN1lAmCu9ADgX68prdWlVoLGgLS3A\x3d\x3d";
    var Le = {
        __cl: !0,
        __ecl: !0,
        __ehl: !0,
        __evl: !0,
        __fal: !0,
        __fil: !0,
        __fsl: !0,
        __hl: !0,
        __jel: !0,
        __lcl: !0,
        __sdl: !0,
        __tl: !0,
        __ytl: !0
    }, Me = {__paused: !0, __tg: !0}, Ne;
    for (Ne in Le) Le.hasOwnProperty(Ne) && (Me[Ne] = !0);
    var Oe = "www.googletagmanager.com/gtm.js";
    Oe = "www.googletagmanager.com/gtag/js";
    var Pe = Oe, Qe = Wa("true"), Re = null, Se = null, Te = "//www.googletagmanager.com/a?id=" + Je.D + "&cv=1",
        Ue = {}, Ve = {}, We = function () {
            var a = L.sequence || 1;
            L.sequence = a + 1;
            return a
        };
    var Xe = {}, Ye = new La, Ze = {}, $e = {}, cf = {
        name: "dataLayer", set: function (a, b) {
            m(ib(a, b), Ze);
            af()
        }, get: function (a) {
            return bf(a, 2)
        }, reset: function () {
            Ye = new La;
            Ze = {};
            af()
        }
    }, bf = function (a, b) {
        return 2 != b ? Ye.get(a) : df(a)
    }, df = function (a) {
        var b, c = a.split(".");
        b = b || [];
        for (var d = Ze, e = 0; e < c.length; e++) {
            if (null === d) return !1;
            if (void 0 === d) break;
            d = d[c[e]];
            if (-1 !== Ia(b, d)) return
        }
        return d
    }, ef = function (a, b) {
        $e.hasOwnProperty(a) || (Ye.set(a, b), m(ib(a, b), Ze), af())
    }, af = function (a) {
        Oa($e, function (b, c) {
            Ye.set(b, c);
            m(ib(b,
                void 0), Ze);
            m(ib(b, c), Ze);
            a && delete $e[b]
        })
    }, ff = function (a, b, c) {
        Xe[a] = Xe[a] || {};
        var d = 1 !== c ? df(b) : Ye.get(b);
        "array" === lb(d) || "object" === lb(d) ? Xe[a][b] = m(d) : Xe[a][b] = d
    }, gf = function (a, b) {
        if (Xe[a]) return Xe[a][b]
    }, hf = function (a, b) {
        Xe[a] && delete Xe[a][b]
    };
    var lf = {}, mf = function (a, b) {
        if (G._gtmexpgrp && G._gtmexpgrp.hasOwnProperty(a)) return G._gtmexpgrp[a];
        void 0 === lf[a] && (lf[a] = Math.floor(Math.random() * b));
        return lf[a]
    };
    var nf = function (a) {
        var b = 1, c, d, e;
        if (a) for (b = 0, d = a.length - 1; 0 <= d; d--) e = a.charCodeAt(d), b = (b << 6 & 268435455) + e + (e << 14), c = b & 266338304, b = 0 != c ? b ^ c >> 21 : b;
        return b
    };

    function qf(a, b, c) {
        for (var d = [], e = b.split(";"), f = 0; f < e.length; f++) {
            var h = e[f].split("="), k = h[0].replace(/^\s*|\s*$/g, "");
            if (k && k == a) {
                var l = h.slice(1).join("=").replace(/^\s*|\s*$/g, "");
                l && c && (l = decodeURIComponent(l));
                d.push(l)
            }
        }
        return d
    };var sf = function (a, b, c, d) {
        return rf(d) ? qf(a, String(b || document.cookie), c) : []
    }, vf = function (a, b, c, d, e) {
        if (rf(e)) {
            var f = tf(a, d, e);
            if (1 === f.length) return f[0].id;
            if (0 !== f.length) {
                f = uf(f, function (h) {
                    return h.xc
                }, b);
                if (1 === f.length) return f[0].id;
                f = uf(f, function (h) {
                    return h.Ob
                }, c);
                return f[0] ? f[0].id : void 0
            }
        }
    };

    function wf(a, b, c, d) {
        var e = document.cookie;
        document.cookie = a;
        var f = document.cookie;
        return e != f || void 0 != c && 0 <= sf(b, f, !1, d).indexOf(c)
    }

    var Af = function (a, b, c) {
        function d(t, r, v) {
            if (null == v) return delete h[r], t;
            h[r] = v;
            return t + "; " + r + "=" + v
        }

        function e(t, r) {
            if (null == r) return delete h[r], t;
            h[r] = !0;
            return t + "; " + r
        }

        if (!rf(c.va)) return 2;
        var f;
        void 0 == b ? f = a + "=deleted; expires=" + (new Date(0)).toUTCString() : (c.encode && (b = encodeURIComponent(b)), b = xf(b), f = a + "=" + b);
        var h = {};
        f = d(f, "path", c.path);
        var k;
        c.expires instanceof Date ? k = c.expires.toUTCString() : null != c.expires && (k = "" + c.expires);
        f = d(f, "expires", k);
        f = d(f, "max-age", c.Mi);
        f = d(f, "samesite",
            c.Qi);
        c.Ri && (f = e(f, "secure"));
        var l = c.domain;
        if ("auto" === l) {
            for (var p = yf(), q = 0; q < p.length; ++q) {
                var n = "none" !== p[q] ? p[q] : void 0, u = d(f, "domain", n);
                u = e(u, c.flags);
                if (!zf(n, c.path) && wf(u, a, b, c.va)) return 0
            }
            return 1
        }
        l && "none" !== l && (f = d(f, "domain", l));
        f = e(f, c.flags);
        return zf(l, c.path) ? 1 : wf(f, a, b, c.va) ? 0 : 1
    }, Bf = function (a, b, c) {
        null == c.path && (c.path = "/");
        c.domain || (c.domain = "auto");
        return Af(a, b, c)
    };

    function uf(a, b, c) {
        for (var d = [], e = [], f, h = 0; h < a.length; h++) {
            var k = a[h], l = b(k);
            l === c ? d.push(k) : void 0 === f || l < f ? (e = [k], f = l) : l === f && e.push(k)
        }
        return 0 < d.length ? d : e
    }

    function tf(a, b, c) {
        for (var d = [], e = sf(a, void 0, void 0, c), f = 0; f < e.length; f++) {
            var h = e[f].split("."), k = h.shift();
            if (!b || -1 !== b.indexOf(k)) {
                var l = h.shift();
                l && (l = l.split("-"), d.push({id: h.join("."), xc: 1 * l[0] || 1, Ob: 1 * l[1] || 1}))
            }
        }
        return d
    }

    var xf = function (a) {
        a && 1200 < a.length && (a = a.substring(0, 1200));
        return a
    }, Cf = /^(www\.)?google(\.com?)?(\.[a-z]{2})?$/, Df = /(^|\.)doubleclick\.net$/i, zf = function (a, b) {
        return Df.test(document.location.hostname) || "/" === b && Cf.test(a)
    }, yf = function () {
        var a = [], b = document.location.hostname.split(".");
        if (4 === b.length) {
            var c = b[b.length - 1];
            if (parseInt(c, 10).toString() === c) return ["none"]
        }
        for (var d = b.length - 2; 0 <= d; d--) a.push(b.slice(d).join("."));
        var e = document.location.hostname;
        Df.test(e) || Cf.test(e) || a.push("none");
        return a
    }, rf = function (a) {
        if (!td() || !a || !Ed()) return !0;
        if (!Dd(a)) return !1;
        var b = Bd(a);
        return null == b ? !0 : !!b
    };
    var Ef = function () {
        for (var a = ad.userAgent + (H.cookie || "") + (H.referrer || ""), b = a.length, c = G.history.length; 0 < c;) a += c-- ^ b++;
        return [Math.round(2147483647 * Math.random()) ^ nf(a) & 2147483647, Math.round(Za() / 1E3)].join(".")
    }, Hf = function (a, b, c, d, e) {
        var f = Ff(b);
        return vf(a, f, Gf(c), d, e)
    }, If = function (a, b, c, d) {
        var e = "" + Ff(c), f = Gf(d);
        1 < f && (e += "-" + f);
        return [b, e, a].join(".")
    }, Ff = function (a) {
        if (!a) return 1;
        a = 0 === a.indexOf(".") ? a.substr(1) : a;
        return a.split(".").length
    }, Gf = function (a) {
        if (!a || "/" === a) return 1;
        "/" !== a[0] &&
        (a = "/" + a);
        "/" !== a[a.length - 1] && (a += "/");
        return a.split("/").length - 1
    };

    function Jf(a, b, c) {
        var d, e = a.Nb;
        null == e && (e = 7776E3);
        0 !== e && (d = new Date((b || Za()) + 1E3 * e));
        return {path: a.path, domain: a.domain, flags: a.flags, encode: !!c, expires: d}
    };var Kf = ["1"], Lf = {}, Of = function (a) {
        var b = Mf(a.prefix), c = Lf[b];
        c && Nf(b, c, a)
    }, Qf = function (a) {
        var b = Mf(a.prefix);
        if (!Lf[b] && !Pf(b, a.path, a.domain)) {
            var c = Ef();
            if (0 === Nf(b, c, a)) {
                var d = cd("google_tag_data", {});
                d._gcl_au ? za("GTM", 57) : d._gcl_au = c
            }
            Pf(b, a.path, a.domain)
        }
    };

    function Nf(a, b, c) {
        var d = If(b, "1", c.domain, c.path), e = Jf(c);
        e.va = "ad_storage";
        return Bf(a, d, e)
    }

    function Pf(a, b, c) {
        var d = Hf(a, b, c, Kf, "ad_storage");
        d && (Lf[a] = d);
        return d
    }

    function Mf(a) {
        return (a || "_gcl") + "_au"
    };

    function Rf() {
        for (var a = Sf, b = {}, c = 0; c < a.length; ++c) b[a[c]] = c;
        return b
    }

    function Tf() {
        var a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        a += a.toLowerCase() + "0123456789-_";
        return a + "."
    }

    var Sf, Uf;

    function Vf(a) {
        function b(l) {
            for (; d < a.length;) {
                var p = a.charAt(d++), q = Uf[p];
                if (null != q) return q;
                if (!/^[\s\xa0]*$/.test(p)) throw Error("Unknown base64 encoding at char: " + p);
            }
            return l
        }

        Sf = Sf || Tf();
        Uf = Uf || Rf();
        for (var c = "", d = 0; ;) {
            var e = b(-1), f = b(0), h = b(64), k = b(64);
            if (64 === k && -1 === e) return c;
            c += String.fromCharCode(e << 2 | f >> 4);
            64 != h && (c += String.fromCharCode(f << 4 & 240 | h >> 2), 64 != k && (c += String.fromCharCode(h << 6 & 192 | k)))
        }
    };var Wf;
    var $f = function () {
        var a = Xf, b = Yf, c = Zf(), d = function (h) {
            a(h.target || h.srcElement || {})
        }, e = function (h) {
            b(h.target || h.srcElement || {})
        };
        if (!c.init) {
            kd(H, "mousedown", d);
            kd(H, "keyup", d);
            kd(H, "submit", e);
            var f = HTMLFormElement.prototype.submit;
            HTMLFormElement.prototype.submit = function () {
                b(this);
                f.call(this)
            };
            c.init = !0
        }
    }, ag = function (a, b, c, d, e) {
        var f = {callback: a, domains: b, fragment: 2 === c, placement: c, forms: d, sameHost: e};
        Zf().decorators.push(f)
    }, bg = function (a, b, c) {
        for (var d = Zf().decorators, e = {}, f = 0; f < d.length; ++f) {
            var h =
                d[f], k;
            if (k = !c || h.forms) a:{
                var l = h.domains, p = a, q = !!h.sameHost;
                if (l && (q || p !== H.location.hostname)) for (var n = 0; n < l.length; n++) if (l[n] instanceof RegExp) {
                    if (l[n].test(p)) {
                        k = !0;
                        break a
                    }
                } else if (0 <= p.indexOf(l[n]) || q && 0 <= l[n].indexOf(p)) {
                    k = !0;
                    break a
                }
                k = !1
            }
            if (k) {
                var u = h.placement;
                void 0 == u && (u = h.fragment ? 2 : 1);
                u === b && db(e, h.callback())
            }
        }
        return e
    }, Zf = function () {
        var a = cd("google_tag_data", {}), b = a.gl;
        b && b.decorators || (b = {decorators: []}, a.gl = b);
        return b
    };
    var cg = /(.*?)\*(.*?)\*(.*)/, dg = /^https?:\/\/([^\/]*?)\.?cdn\.ampproject\.org\/?(.*)/,
        eg = /^(?:www\.|m\.|amp\.)+/, fg = /([^?#]+)(\?[^#]*)?(#.*)?/;

    function gg(a) {
        return new RegExp("(.*?)(^|&)" + a + "=([^&]*)&?(.*)")
    }

    var ig = function (a) {
        var b = [], c;
        for (c in a) if (a.hasOwnProperty(c)) {
            var d = a[c];
            if (void 0 !== d && d === d && null !== d && "[object Object]" !== d.toString()) {
                b.push(c);
                var e = b, f = e.push, h, k = String(d);
                Sf = Sf || Tf();
                Uf = Uf || Rf();
                for (var l = [], p = 0; p < k.length; p += 3) {
                    var q = p + 1 < k.length, n = p + 2 < k.length, u = k.charCodeAt(p),
                        t = q ? k.charCodeAt(p + 1) : 0, r = n ? k.charCodeAt(p + 2) : 0, v = u >> 2,
                        w = (u & 3) << 4 | t >> 4, y = (t & 15) << 2 | r >> 6, x = r & 63;
                    n || (x = 64, q || (y = 64));
                    l.push(Sf[v], Sf[w], Sf[y], Sf[x])
                }
                h = l.join("");
                f.call(e, h)
            }
        }
        var A = b.join("*");
        return ["1", hg(A),
            A].join("*")
    }, hg = function (a, b) {
        var c = [window.navigator.userAgent, (new Date).getTimezoneOffset(), window.navigator.userLanguage || window.navigator.language, Math.floor((new Date).getTime() / 60 / 1E3) - (void 0 === b ? 0 : b), a].join("*"),
            d;
        if (!(d = Wf)) {
            for (var e = Array(256), f = 0; 256 > f; f++) {
                for (var h = f, k = 0; 8 > k; k++) h = h & 1 ? h >>> 1 ^ 3988292384 : h >>> 1;
                e[f] = h
            }
            d = e
        }
        Wf = d;
        for (var l = 4294967295, p = 0; p < c.length; p++) l = l >>> 8 ^ Wf[(l ^ c.charCodeAt(p)) & 255];
        return ((l ^ -1) >>> 0).toString(36)
    }, kg = function () {
        return function (a) {
            var b = me(G.location.href),
                c = b.search.replace("?", ""), d = he(c, "_gl", !0) || "";
            a.query = jg(d) || {};
            var e = ke(b, "fragment").match(gg("_gl"));
            a.fragment = jg(e && e[3] || "") || {}
        }
    }, lg = function (a) {
        var b = kg(), c = Zf();
        c.data || (c.data = {query: {}, fragment: {}}, b(c.data));
        var d = {}, e = c.data;
        e && (db(d, e.query), a && db(d, e.fragment));
        return d
    }, jg = function (a) {
        var b;
        b = void 0 === b ? 3 : b;
        try {
            if (a) {
                var c;
                a:{
                    for (var d = a, e = 0; 3 > e; ++e) {
                        var f = cg.exec(d);
                        if (f) {
                            c = f;
                            break a
                        }
                        d = decodeURIComponent(d)
                    }
                    c = void 0
                }
                var h = c;
                if (h && "1" === h[1]) {
                    var k = h[3], l;
                    a:{
                        for (var p = h[2], q = 0; q <
                        b; ++q) if (p === hg(k, q)) {
                            l = !0;
                            break a
                        }
                        l = !1
                    }
                    if (l) {
                        for (var n = {}, u = k ? k.split("*") : [], t = 0; t < u.length; t += 2) n[u[t]] = Vf(u[t + 1]);
                        return n
                    }
                }
            }
        } catch (r) {
        }
    };

    function mg(a, b, c, d) {
        function e(q) {
            var n = q, u = gg(a).exec(n), t = n;
            if (u) {
                var r = u[2], v = u[4];
                t = u[1];
                v && (t = t + r + v)
            }
            q = t;
            var w = q.charAt(q.length - 1);
            q && "&" !== w && (q += "&");
            return q + p
        }

        d = void 0 === d ? !1 : d;
        var f = fg.exec(c);
        if (!f) return "";
        var h = f[1], k = f[2] || "", l = f[3] || "", p = a + "=" + b;
        d ? l = "#" + e(l.substring(1)) : k = "?" + e(k.substring(1));
        return "" + h + k + l
    }

    function ng(a, b) {
        var c = "FORM" === (a.tagName || "").toUpperCase(), d = bg(b, 1, c), e = bg(b, 2, c), f = bg(b, 3, c);
        if (fb(d)) {
            var h = ig(d);
            c ? og("_gl", h, a) : pg("_gl", h, a, !1)
        }
        if (!c && fb(e)) {
            var k = ig(e);
            pg("_gl", k, a, !0)
        }
        for (var l in f) if (f.hasOwnProperty(l)) a:{
            var p = l, q = f[l], n = a;
            if (n.tagName) {
                if ("a" === n.tagName.toLowerCase()) {
                    pg(p, q, n, void 0);
                    break a
                }
                if ("form" === n.tagName.toLowerCase()) {
                    og(p, q, n);
                    break a
                }
            }
            "string" == typeof n && mg(p, q, n, void 0)
        }
    }

    function pg(a, b, c, d) {
        if (c.href) {
            var e = mg(a, b, c.href, void 0 === d ? !1 : d);
            Pc.test(e) && (c.href = e)
        }
    }

    function og(a, b, c) {
        if (c && c.action) {
            var d = (c.method || "").toLowerCase();
            if ("get" === d) {
                for (var e = c.childNodes || [], f = !1, h = 0; h < e.length; h++) {
                    var k = e[h];
                    if (k.name === a) {
                        k.setAttribute("value", b);
                        f = !0;
                        break
                    }
                }
                if (!f) {
                    var l = H.createElement("input");
                    l.setAttribute("type", "hidden");
                    l.setAttribute("name", a);
                    l.setAttribute("value", b);
                    c.appendChild(l)
                }
            } else if ("post" === d) {
                var p = mg(a, b, c.action);
                Pc.test(p) && (c.action = p)
            }
        }
    }

    var Xf = function (a) {
        try {
            var b;
            a:{
                for (var c = a, d = 100; c && 0 < d;) {
                    if (c.href && c.nodeName.match(/^a(?:rea)?$/i)) {
                        b = c;
                        break a
                    }
                    c = c.parentNode;
                    d--
                }
                b = null
            }
            var e = b;
            if (e) {
                var f = e.protocol;
                "http:" !== f && "https:" !== f || ng(e, e.hostname)
            }
        } catch (h) {
        }
    }, Yf = function (a) {
        try {
            if (a.action) {
                var b = ke(me(a.action), "host");
                ng(a, b)
            }
        } catch (c) {
        }
    }, qg = function (a, b, c, d) {
        $f();
        ag(a, b, "fragment" === c ? 2 : 1, !!d, !1)
    }, rg = function (a, b) {
        $f();
        ag(a, [je(G.location, "host", !0)], b, !0, !0)
    }, sg = function () {
        var a = H.location.hostname, b = dg.exec(H.referrer);
        if (!b) return !1;
        var c = b[2], d = b[1], e = "";
        if (c) {
            var f = c.split("/"), h = f[1];
            e = "s" === h ? decodeURIComponent(f[2]) : decodeURIComponent(h)
        } else if (d) {
            if (0 === d.indexOf("xn--")) return !1;
            e = d.replace(/-/g, ".").replace(/\.\./g, "-")
        }
        var k = a.replace(eg, ""), l = e.replace(eg, ""), p;
        if (!(p = k === l)) {
            var q = "." + l;
            p = k.substring(k.length - q.length, k.length) === q
        }
        return p
    }, tg = function (a, b) {
        return !1 === a ? !1 : a || b || sg()
    };
    var ug = /^\w+$/, vg = /^[\w-]+$/, wg = /^~?[\w-]+$/, xg = {aw: "_aw", dc: "_dc", gf: "_gf", ha: "_ha", gp: "_gp"},
        yg = function () {
            if (!td() || !Ed()) return !0;
            var a = Bd("ad_storage");
            return null == a ? !0 : !!a
        }, zg = function (a, b) {
            Dd("ad_storage") ? yg() ? a() : Id(a, "ad_storage") : b ? za("TAGGING", 3) : Hd(function () {
                zg(a, !0)
            }, ["ad_storage"])
        }, Bg = function (a) {
            return Ag(a).map(function (b) {
                return b.Ia
            })
        }, Ag = function (a) {
            var b = [];
            if (!H.cookie) return b;
            var c = sf(a, H.cookie, void 0, "ad_storage");
            if (!c || 0 == c.length) return b;
            for (var d = {}, e = 0; e < c.length; d =
                {lb: d.lb}, e++) {
                d.lb = Cg(c[e]);
                var f = Dg(c[e]);
                if (d.lb && f) {
                    var h = Ja(b, function (k) {
                        return function (l) {
                            return l.Ia === k.lb
                        }
                    }(d));
                    h && h.timestamp < f ? h.timestamp = f : h || b.push({Ia: d.lb, timestamp: f})
                }
            }
            return Eg(b)
        };

    function Fg(a) {
        return a && "string" == typeof a && a.match(ug) ? a : "_gcl"
    }

    var Hg = function () {
        var a = me(G.location.href), b = ke(a, "query", !1, void 0, "gclid"), c = ke(a, "query", !1, void 0, "gclsrc"),
            d = ke(a, "query", !1, void 0, "dclid");
        if (!b || !c) {
            var e = a.hash.replace("#", "");
            b = b || he(e, "gclid", void 0);
            c = c || he(e, "gclsrc", void 0)
        }
        return Gg(b, c, d)
    }, Gg = function (a, b, c) {
        var d = {}, e = function (f, h) {
            d[h] || (d[h] = []);
            d[h].push(f)
        };
        d.gclid = a;
        d.gclsrc = b;
        d.dclid = c;
        if (void 0 !== a && a.match(vg)) switch (b) {
            case void 0:
                e(a, "aw");
                break;
            case "aw.ds":
                e(a, "aw");
                e(a, "dc");
                break;
            case "ds":
                e(a, "dc");
                break;
            case "3p.ds":
                e(a,
                    "dc");
                break;
            case "gf":
                e(a, "gf");
                break;
            case "ha":
                e(a, "ha");
                break;
            case "gp":
                e(a, "gp")
        }
        c && e(c, "dc");
        return d
    }, Ig = function (a, b) {
        switch (a) {
            case void 0:
            case "aw":
                return "aw" === b;
            case "aw.ds":
                return "aw" === b || "dc" === b;
            case "ds":
            case "3p.ds":
                return "dc" === b;
            case "gf":
                return "gf" === b;
            case "ha":
                return "ha" === b;
            case "gp":
                return "gp" === b
        }
        return !1
    }, Kg = function (a) {
        var b = Hg();
        zg(function () {
            Jg(b, a)
        })
    };

    function Jg(a, b, c) {
        function d(l, p) {
            var q = Lg(l, e);
            q && Bf(q, p, f)
        }

        b = b || {};
        var e = Fg(b.prefix);
        c = c || Za();
        var f = Jf(b, c, !0);
        f.va = "ad_storage";
        var h = Math.round(c / 1E3), k = function (l) {
            return ["GCL", h, l].join(".")
        };
        a.aw && (!0 === b.Wi ? d("aw", k("~" + a.aw[0])) : d("aw", k(a.aw[0])));
        a.dc && d("dc", k(a.dc[0]));
        a.gf && d("gf", k(a.gf[0]));
        a.ha && d("ha", k(a.ha[0]));
        a.gp && d("gp", k(a.gp[0]))
    }

    var Mg = function (a, b) {
        var c = lg(!0);
        zg(function () {
            for (var d = Fg(b.prefix), e = 0; e < a.length; ++e) {
                var f = a[e];
                if (void 0 !== xg[f]) {
                    var h = Lg(f, d), k = c[h];
                    if (k) {
                        var l = Math.min(Dg(k), Za()), p;
                        b:{
                            for (var q = l, n = sf(h, H.cookie, void 0, "ad_storage"), u = 0; u < n.length; ++u) if (Dg(n[u]) > q) {
                                p = !0;
                                break b
                            }
                            p = !1
                        }
                        if (!p) {
                            var t = Jf(b, l, !0);
                            t.va = "ad_storage";
                            Bf(h, k, t)
                        }
                    }
                }
            }
            Jg(Gg(c.gclid, c.gclsrc), b)
        })
    }, Lg = function (a, b) {
        var c = xg[a];
        if (void 0 !== c) return b + c
    }, Dg = function (a) {
        var b = a.split(".");
        return 3 !== b.length || "GCL" !== b[0] ? 0 : 1E3 * (Number(b[1]) ||
            0)
    };

    function Cg(a) {
        var b = a.split(".");
        if (3 == b.length && "GCL" == b[0] && b[1]) return b[2]
    }

    var Ng = function (a, b, c, d, e) {
        if (Ha(b)) {
            var f = Fg(e), h = function () {
                for (var k = {}, l = 0; l < a.length; ++l) {
                    var p = Lg(a[l], f);
                    if (p) {
                        var q = sf(p, H.cookie, void 0, "ad_storage");
                        q.length && (k[p] = q.sort()[q.length - 1])
                    }
                }
                return k
            };
            zg(function () {
                qg(h, b, c, d)
            })
        }
    }, Eg = function (a) {
        return a.filter(function (b) {
            return wg.test(b.Ia)
        })
    }, Og = function (a, b) {
        for (var c = Fg(b.prefix), d = {}, e = 0; e < a.length; e++) xg[a[e]] && (d[a[e]] = xg[a[e]]);
        zg(function () {
            Oa(d, function (f, h) {
                var k = sf(c + h, H.cookie, void 0, "ad_storage");
                if (k.length) {
                    var l = k[0], p = Dg(l),
                        q = {};
                    q[f] = [Cg(l)];
                    Jg(q, b, p)
                }
            })
        })
    };

    function Pg(a, b) {
        for (var c = 0; c < b.length; ++c) if (a[b[c]]) return !0;
        return !1
    }

    var Qg = function () {
        function a(e, f, h) {
            h && (e[f] = h)
        }

        var b = ["aw", "dc"];
        if (Ed()) {
            var c = Hg();
            if (Pg(c, b)) {
                var d = {};
                a(d, "gclid", c.gclid);
                a(d, "dclid", c.dclid);
                a(d, "gclsrc", c.gclsrc);
                rg(function () {
                    return d
                }, 3);
                rg(function () {
                    var e = {};
                    return e._up = "1", e
                }, 1)
            }
        }
    }, Rg = function () {
        var a;
        if (yg()) {
            for (var b = [], c = H.cookie.split(";"), d = /^\s*_gac_(UA-\d+-\d+)=\s*(.+?)\s*$/, e = 0; e < c.length; e++) {
                var f = c[e].match(d);
                f && b.push({Vd: f[1], value: f[2]})
            }
            var h = {};
            if (b && b.length) for (var k = 0; k < b.length; k++) {
                var l = b[k].value.split(".");
                "1" == l[0] && 3 == l.length && l[1] && (h[b[k].Vd] || (h[b[k].Vd] = []), h[b[k].Vd].push({
                    timestamp: l[1],
                    Ia: l[2]
                }))
            }
            a = h
        } else a = {};
        return a
    };
    var Sg = /^\d+\.fls\.doubleclick\.net$/, Tg = !1;

    function Ug(a, b) {
        Dd(C.B) ? Nd(C.B) ? a() : Id(a, C.B) : b ? F(42) : Pd(function () {
            Ug(a, !0)
        }, [C.B])
    }

    function Vg(a) {
        var b = me(G.location.href), c = ke(b, "host", !1);
        if (c && c.match(Sg)) {
            var d = ke(b, "path").split(a + "=");
            if (1 < d.length) return d[1].split(";")[0].split("?")[0]
        }
    }

    function Wg(a, b, c) {
        if ("aw" == a || "dc" == a) {
            var d = Vg("gcl" + a);
            if (d) return d.split(".")
        }
        var e = Fg(b);
        if ("_gcl" == e) {
            c = void 0 === c ? !0 : c;
            var f = !Nd(C.B) && c, h;
            h = Hg()[a] || [];
            if (0 < h.length) return f ? ["0"] : h
        }
        var k = Lg(a, e);
        return k ? Bg(k) : []
    }

    var Xg = function (a) {
        var b = Vg("gac");
        if (b) return !Nd(C.B) && a ? "0" : decodeURIComponent(b);
        var c = Rg(), d = [];
        Oa(c, function (e, f) {
            f = Eg(f);
            for (var h = [], k = 0; k < f.length; k++) h.push(f[k].Ia);
            h.length && d.push(e + ":" + h.join(","))
        });
        return d.join(";")
    }, Zg = function (a, b) {
        if (Tg) Yg(a, b, "dc"); else {
            var c = Hg().dc || [];
            Ug(function () {
                Qf(b);
                var d = Lf[Mf(b.prefix)], e = !1;
                if (d && 0 < c.length) {
                    var f = L.joined_au = L.joined_au || {}, h = b.prefix || "_gcl";
                    if (!f[h]) for (var k = 0; k < c.length; k++) {
                        var l = "http://ad.doubleclick.net/ddm/regclk";
                        l = l + "?gclid=" + c[k] + "&auiddc=" +
                            d;
                        qd(l);
                        e = f[h] = !0
                    }
                }
                null == a && (a = e);
                a && d && Of(b)
            })
        }
    }, Yg = function (a, b, c) {
        var d = Hg(), e = [], f = d.gclid, h = d.dclid, k = d.gclsrc || "aw";
        !f || "aw.ds" !== k && "aw" !== k && "ds" !== k || c && !Ig(k, c) || e.push({Ia: f, vf: k});
        !h || c && "dc" !== c || e.push({Ia: h, vf: "ds"});
        Ug(function () {
            Qf(b);
            var l = Lf[Mf(b.prefix)], p = !1;
            if (l && 0 < e.length) for (var q = L.joined_auid = L.joined_auid || {}, n = 0; n < e.length; n++) {
                var u = e[n], t = u.Ia, r = u.vf, v = (b.prefix || "_gcl") + "." + r + "." + t;
                if (!q[v]) {
                    var w = "http://ad.doubleclick.net/pagead/regclk";
                    w = w + "?gclid=" + t + "&auid=" + l + "&gclsrc=" + r;
                    qd(w);
                    p = q[v] = !0
                }
            }
            null == a && (a = p);
            a && l && Of(b)
        })
    };
    var $g = /[A-Z]+/, ah = /\s/, bh = function (a) {
        if (g(a) && (a = Ya(a), !ah.test(a))) {
            var b = a.indexOf("-");
            if (!(0 > b)) {
                var c = a.substring(0, b);
                if ($g.test(c)) {
                    for (var d = a.substring(b + 1).split("/"), e = 0; e < d.length; e++) if (!d[e]) return;
                    return {id: a, prefix: c, containerId: c + "-" + d[0], F: d}
                }
            }
        }
    }, dh = function (a) {
        for (var b = {}, c = 0; c < a.length; ++c) {
            var d = bh(a[c]);
            d && (b[d.id] = d)
        }
        ch(b);
        var e = [];
        Oa(b, function (f, h) {
            e.push(h)
        });
        return e
    };

    function ch(a) {
        var b = [], c;
        for (c in a) if (a.hasOwnProperty(c)) {
            var d = a[c];
            "AW" === d.prefix && d.F[1] && b.push(d.containerId)
        }
        for (var e = 0; e < b.length; ++e) delete a[b[e]]
    };var eh = function () {
        var a = !1;
        return a
    };
    var gh = function (a, b, c, d) {
        return (2 === fh() || d || "http:" != G.location.protocol ? a : b) + c
    }, fh = function () {
        var a = hd(), b;
        if (1 === a) a:{
            var c = Pe;
            c = c.toLowerCase();
            for (var d = "https://" + c, e = "http://" + c, f = 1, h = H.getElementsByTagName("script"), k = 0; k < h.length && 100 > k; k++) {
                var l = h[k].src;
                if (l) {
                    l = l.toLowerCase();
                    if (0 === l.indexOf(e)) {
                        b = 3;
                        break a
                    }
                    1 === f && 0 === l.indexOf(d) && (f = 2)
                }
            }
            b = f
        } else b = a;
        return b
    };
    var ih = function (a, b, c) {
            if (G[a.functionName]) return b.Hd && J(b.Hd), G[a.functionName];
            var d = hh();
            G[a.functionName] = d;
            if (a.qc) for (var e = 0; e < a.qc.length; e++) G[a.qc[e]] = G[a.qc[e]] || hh();
            a.Ac && void 0 === G[a.Ac] && (G[a.Ac] = c);
            gd(gh("https://", "http://", a.Sd), b.Hd, b.Th);
            return d
        }, hh = function () {
            var a = function () {
                a.q = a.q || [];
                a.q.push(arguments)
            };
            return a
        }, jh = {functionName: "_googWcmImpl", Ac: "_googWcmAk", Sd: "www.gstatic.com/wcm/loader.js"},
        kh = {functionName: "_gaPhoneImpl", Ac: "ga_wpid", Sd: "www.gstatic.com/gaphone/loader.js"},
        lh = {Of: "", Lg: "5"}, mh = {
            functionName: "_googCallTrackingImpl",
            qc: [kh.functionName, jh.functionName],
            Sd: "www.gstatic.com/call-tracking/call-tracking_" + (lh.Of || lh.Lg) + ".js"
        }, nh = {}, oh = function (a, b, c, d) {
            F(22);
            if (c) {
                d = d || {};
                var e = ih(jh, d, a), f = {ak: a, cl: b};
                void 0 === d.qa && (f.autoreplace = c);
                e(2, d.qa, f, c, 0, new Date, d.options)
            }
        }, ph = function (a, b, c, d) {
            F(21);
            if (b && c) {
                d = d || {};
                for (var e = {
                    countryNameCode: c,
                    destinationNumber: b,
                    retrievalTime: new Date
                }, f = 0; f < a.length; f++) {
                    var h = a[f];
                    nh[h.id] || (h && "AW" === h.prefix && !e.adData && 2 <= h.F.length ? (e.adData = {
                        ak: h.F[0],
                        cl: h.F[1]
                    }, nh[h.id] = !0) : h && "UA" === h.prefix && !e.gaData && (e.gaData = {gaWpid: h.containerId}, nh[h.id] = !0))
                }
                (e.gaData || e.adData) && ih(mh, d)(d.qa, e, d.options)
            }
        }, qh = function () {
            var a = !1;
            return a
        }, rh = function (a, b) {
            if (a) if (eh()) {
            } else {
                if (g(a)) {
                    var c =
                        bh(a);
                    if (!c) return;
                    a = c
                }
                var d = void 0, e = !1, f = b.getWithConfig(C.sg);
                if (f && Ha(f)) {
                    d = [];
                    for (var h = 0; h < f.length; h++) {
                        var k = bh(f[h]);
                        k && (d.push(k), (a.id === k.id || a.id === a.containerId && a.containerId === k.containerId) && (e = !0))
                    }
                }
                if (!d || e) {
                    var l = b.getWithConfig(C.Ie), p;
                    if (l) {
                        Ha(l) ? p = l : p = [l];
                        var q = b.getWithConfig(C.Ge), n = b.getWithConfig(C.He), u = b.getWithConfig(C.Je),
                            t = b.getWithConfig(C.rg), r = q || n, v = 1;
                        "UA" !== a.prefix || d || (v = 5);
                        for (var w = 0; w < p.length; w++) if (w < v) if (d) ph(d, p[w], t, {
                            qa: r,
                            options: u
                        }); else if ("AW" === a.prefix &&
                            a.F[1]) qh() ? ph([a], p[w], t || "US", {qa: r, options: u}) : oh(a.F[0], a.F[1], p[w], {
                            qa: r,
                            options: u
                        }); else if ("UA" === a.prefix) if (qh()) ph([a], p[w], t || "US", {qa: r}); else {
                            var y = a.containerId, x = p[w], A = {qa: r};
                            F(23);
                            if (x) {
                                A = A || {};
                                var z = ih(kh, A, y), B = {};
                                void 0 !== A.qa ? B.receiver = A.qa : B.replace = x;
                                B.ga_wpid = y;
                                B.destination = x;
                                z(2, new Date, B)
                            }
                        }
                    }
                }
            }
        };
    var uh = function (a) {
        return Nd(C.B) ? a : a.replace(/&url=([^&#]+)/, function (b, c) {
            var d = ne(decodeURIComponent(c));
            return "&url=" + encodeURIComponent(d)
        })
    }, vh = function () {
        var a;
        if (!(a = Qe)) {
            var b;
            if (!0 === G._gtmdgs) b = !0; else {
                var c = ad && ad.userAgent || "";
                b = 0 > c.indexOf("Safari") || /Chrome|Coast|Opera|Edg|Silk|Android/.test(c) || 11 > ((/Version\/([\d]+)/.exec(c) || [])[1] || "") ? !1 : !0
            }
            a = !b
        }
        if (a) return -1;
        var d = Sa("1");
        return mf(1, 100) < d ? mf(2, 2) : -1
    }, wh = function (a) {
        var b;
        if (!a || !a.length) return;
        for (var c = [], d = 0; d < a.length; ++d) {
            var e = a[d];
            e && e.estimated_delivery_date ? c.push("" + e.estimated_delivery_date) : c.push("")
        }
        b = c.join(",");
        return b
    };
    var xh = new RegExp(/^(.*\.)?(google|youtube|blogger|withgoogle)(\.com?)?(\.[a-z]{2})?\.?$/), yh = {
        cl: ["ecl"],
        customPixels: ["nonGooglePixels"],
        ecl: ["cl"],
        ehl: ["hl"],
        hl: ["ehl"],
        html: ["customScripts", "customPixels", "nonGooglePixels", "nonGoogleScripts", "nonGoogleIframes"],
        customScripts: ["html", "customPixels", "nonGooglePixels", "nonGoogleScripts", "nonGoogleIframes"],
        nonGooglePixels: [],
        nonGoogleScripts: ["nonGooglePixels"],
        nonGoogleIframes: ["nonGooglePixels"]
    }, zh = {
        cl: ["ecl"],
        customPixels: ["customScripts", "html"],
        ecl: ["cl"],
        ehl: ["hl"],
        hl: ["ehl"],
        html: ["customScripts"],
        customScripts: ["html"],
        nonGooglePixels: ["customPixels", "customScripts", "html", "nonGoogleScripts", "nonGoogleIframes"],
        nonGoogleScripts: ["customScripts", "html"],
        nonGoogleIframes: ["customScripts", "html", "nonGoogleScripts"]
    }, Ah = "google customPixels customScripts html nonGooglePixels nonGoogleScripts nonGoogleIframes".split(" ");
    var Ch = function (a) {
        var b = bf("gtm.allowlist") || bf("gtm.whitelist");
        b && F(9);
        b = "google gtagfl lcl zone oid op".split(" ");
        var c = b && hb(Xa(b), yh), d = bf("gtm.blocklist") || bf("gtm.blacklist");
        d || (d = bf("tagTypeBlacklist")) &&
        F(3);
        d ? F(8) : d = [];
        Bh() && (d = Xa(d), d.push("nonGooglePixels", "nonGoogleScripts", "sandboxedScripts"));
        0 <= Ia(Xa(d), "google") && F(2);
        var e = d && hb(Xa(d), zh), f = {};
        return function (h) {
            var k = h && h[qb.Na];
            if (!k || "string" != typeof k) return !0;
            k = k.replace(/^_*/, "");
            if (void 0 !== f[k]) return f[k];
            var l = Ve[k] || [], p = a(k, l);
            if (b) {
                var q;
                if (q = p) a:{
                    if (0 > Ia(c, k)) if (l && 0 < l.length) for (var n = 0; n < l.length; n++) {
                        if (0 >
                            Ia(c, l[n])) {
                            F(11);
                            q = !1;
                            break a
                        }
                    } else {
                        q = !1;
                        break a
                    }
                    q = !0
                }
                p = q
            }
            var u = !1;
            if (d) {
                var t = 0 <= Ia(e, k);
                if (t) u = t; else {
                    var r = Na(e, l || []);
                    r && F(10);
                    u = r
                }
            }
            var v = !p || u;
            v || !(0 <= Ia(l, "sandboxedScripts")) || c && -1 !== Ia(c, "sandboxedScripts") || (v = Na(e, Ah));
            return f[k] = v
        }
    }, Bh = function () {
        return xh.test(G.location && G.location.hostname)
    };
    var Dh = {
        active: !0, isAllowed: function () {
            return !0
        }
    }, Eh = function (a) {
        var b = L.zones;
        return b ? b.checkState(Je.D, a) : Dh
    }, Fh = function (a) {
        var b = L.zones;
        !b && a && (b = L.zones = a());
        return b
    };
    var Gh = function () {
    }, Hh = function () {
    };
    var Ih = !1, Nh = 0, Oh = [];

    function Ph(a) {
        if (!Ih) {
            var b = H.createEventObject, c = "complete" == H.readyState, d = "interactive" == H.readyState;
            if (!a || "readystatechange" != a.type || c || !b && d) {
                Ih = !0;
                for (var e = 0; e < Oh.length; e++) J(Oh[e])
            }
            Oh.push = function () {
                for (var f = 0; f < arguments.length; f++) J(arguments[f]);
                return 0
            }
        }
    }

    function Qh() {
        if (!Ih && 140 > Nh) {
            Nh++;
            try {
                H.documentElement.doScroll("left"), Ph()
            } catch (a) {
                G.setTimeout(Qh, 50)
            }
        }
    }

    var Rh = function (a) {
        Ih ? a() : Oh.push(a)
    };
    var Th = function (a, b) {
        this.m = !1;
        this.H = [];
        this.T = {tags: []};
        this.Y = !1;
        this.o = this.C = 0;
        Sh(this, a, b)
    }, Uh = function (a, b, c, d) {
        if (Me.hasOwnProperty(b) || "__zone" === b) return -1;
        var e = {};
        ob(d) && (e = m(d, e));
        e.id = c;
        e.status = "timeout";
        return a.T.tags.push(e) - 1
    }, Vh = function (a, b, c, d) {
        var e = a.T.tags[b];
        e && (e.status = c, e.executionTime = d)
    }, Wh = function (a) {
        if (!a.m) {
            for (var b = a.H, c = 0; c < b.length; c++) b[c]();
            a.m = !0;
            a.H.length = 0
        }
    }, Sh = function (a, b, c) {
        Da(b) && Xh(a, b);
        c && G.setTimeout(function () {
            return Wh(a)
        }, Number(c))
    }, Xh = function (a,
                      b) {
        var c = cb(function () {
            return J(function () {
                b(Je.D, a.T)
            })
        });
        a.m ? c() : a.H.push(c)
    }, Yh = function (a) {
        a.C++;
        return cb(function () {
            a.o++;
            a.Y && a.o >= a.C && Wh(a)
        })
    };
    var Zh = function () {
        function a(d) {
            return !Ga(d) || 0 > d ? 0 : d
        }

        if (!L._li && G.performance && G.performance.timing) {
            var b = G.performance.timing.navigationStart, c = Ga(cf.get("gtm.start")) ? cf.get("gtm.start") : 0;
            L._li = {cst: a(c - b), cbt: a(Se - b)}
        }
    };
    var ci = {}, di = function () {
        return G.GoogleAnalyticsObject && G[G.GoogleAnalyticsObject]
    }, ei = !1;
    var fi = function (a) {
        G.GoogleAnalyticsObject || (G.GoogleAnalyticsObject = a || "ga");
        var b = G.GoogleAnalyticsObject;
        if (G[b]) G.hasOwnProperty(b) || F(12); else {
            var c = function () {
                c.q = c.q || [];
                c.q.push(arguments)
            };
            c.l = Number(new Date);
            G[b] = c
        }
        Zh();
        return G[b]
    }, gi = function (a, b, c, d) {
        b = String(b).replace(/\s+/g, "").split(",");
        var e = di();
        e(a + "require", "linker");
        e(a + "linker:autoLink", b, c, d)
    }, hi = function (a) {
    };
    var ji = function (a) {
    }, ii = function () {
        return G.GoogleAnalyticsObject || "ga"
    }, ki = function (a, b) {
        return function () {
            var c = di(), d = c && c.getByName && c.getByName(a);
            if (d) {
                var e = d.get("sendHitTask");
                d.set("sendHitTask", function (f) {
                    var h = f.get("hitPayload"), k = f.get("hitCallback"), l = 0 > h.indexOf("&tid=" + b);
                    l && (f.set("hitPayload", h.replace(/&tid=UA-[0-9]+-[0-9]+/, "&tid=" +
                        b), !0), f.set("hitCallback", void 0, !0));
                    e(f);
                    l && (f.set("hitPayload", h, !0), f.set("hitCallback", k, !0), f.set("_x_19", void 0, !0), e(f))
                })
            }
        }
    };
    var pi = function () {
            return "&tc=" + Ub.filter(function (a) {
                return a
            }).length
        }, si = function () {
            2022 <= qi().length && ri()
        }, ui = function () {
            ti || (ti = G.setTimeout(ri, 500))
        }, ri = function () {
            ti && (G.clearTimeout(ti), ti = void 0);
            void 0 === vi || wi[vi] && !xi && !yi || (zi[vi] || Ai.Jh() || 0 >= Bi-- ? (F(1), zi[vi] = !0) : (Ai.di(), jd(qi()), wi[vi] = !0, Ci = Di = Ei = yi = xi = ""))
        }, qi = function () {
            var a = vi;
            if (void 0 === a) return "";
            var b = Aa("GTM"), c = Aa("TAGGING");
            return [Fi, wi[a] ? "" : "&es=1", Gi[a], b ? "&u=" + b : "", c ? "&ut=" + c : "", pi(), xi, yi, Ei ? Ei : "", Di, Ci, "&z=0"].join("")
        },
        Hi = function () {
            return [Te, "&v=3&t=t", "&pid=" + Ka(), "&rv=" + Je.nc].join("")
        }, Ii = "0.005000" > Math.random(), Fi = Hi(), Ji = function () {
            Fi = Hi()
        }, wi = {}, xi = "", yi = "", Ci = "", Di = "", Ei = "", vi = void 0, Gi = {}, zi = {}, ti = void 0,
        Ai = function (a, b) {
            var c = 0, d = 0;
            return {
                Jh: function () {
                    if (c < a) return !1;
                    Za() - d >= b && (c = 0);
                    return c >= a
                }, di: function () {
                    Za() - d >= b && (c = 0);
                    c++;
                    d = Za()
                }
            }
        }(2, 1E3), Bi = 1E3, Ki = function (a, b, c) {
            if (Ii && !zi[a] && b) {
                a !== vi && (ri(), vi = a);
                var d, e = String(b[qb.Na] || "").replace(/_/g, "");
                0 === e.indexOf("cvt") && (e = "cvt");
                d = e;
                var f = c + d;
                xi = xi ? xi + "." + f : "&tr=" + f;
                var h = b["function"];
                if (!h) throw Error("Error: No function name given for function call.");
                var k = (Wb[h] ? "1" : "2") + d;
                Ci = Ci ? Ci + "." + k : "&ti=" + k;
                ui();
                si()
            }
        }, Li = function (a, b, c) {
            if (Ii && !zi[a]) {
                a !== vi && (ri(), vi = a);
                var d = c + b;
                yi = yi ? yi + "." + d : "&epr=" + d;
                ui();
                si()
            }
        }, Mi = function (a, b, c) {
        };

    function Ni(a, b, c, d) {
        var e = Ub[a], f = Oi(a, b, c, d);
        if (!f) return null;
        var h = ac(e[qb.af], c, []);
        if (h && h.length) {
            var k = h[0];
            f = Ni(k.index, {onSuccess: f, onFailure: 1 === k.qf ? b.terminate : f, terminate: b.terminate}, c, d)
        }
        return f
    }

    function Oi(a, b, c, d) {
        function e() {
            if (f[qb.Gg]) k(); else {
                var w = bc(f, c, []);
                var A = Uh(c.Oa, String(f[qb.Na]), Number(f[qb.bf]), w[qb.Hg]), z = !1;
                w.vtp_gtmOnSuccess = function () {
                    if (!z) {
                        z = !0;
                        var E = Za() - D;
                        Ki(c.id, Ub[a], "5");
                        Vh(c.Oa, A, "success",
                            E);
                        h()
                    }
                };
                w.vtp_gtmOnFailure = function () {
                    if (!z) {
                        z = !0;
                        var E = Za() - D;
                        Ki(c.id, Ub[a], "6");
                        Vh(c.Oa, A, "failure", E);
                        k()
                    }
                };
                w.vtp_gtmTagId = f.tag_id;
                w.vtp_gtmEventId = c.id;
                Ki(c.id, f, "1");
                var B = function () {
                    var E = Za() - D;
                    Ki(c.id, f, "7");
                    Vh(c.Oa, A, "exception", E);
                    z || (z = !0, k())
                };
                var D = Za();
                try {
                    $b(w, c)
                } catch (E) {
                    B(E)
                }
            }
        }

        var f = Ub[a], h = b.onSuccess, k = b.onFailure, l = b.terminate;
        if (c.Cd(f)) return null;
        var p = ac(f[qb.cf], c, []);
        if (p && p.length) {
            var q = p[0], n = Ni(q.index, {onSuccess: h, onFailure: k, terminate: l}, c, d);
            if (!n) return null;
            h = n;
            k = 2 === q.qf ? l : n
        }
        if (f[qb.Xe] || f[qb.Jg]) {
            var u = f[qb.Xe] ? Vb :
                c.li, t = h, r = k;
            if (!u[a]) {
                e = cb(e);
                var v = Pi(a, u, e);
                h = v.onSuccess;
                k = v.onFailure
            }
            return function () {
                u[a](t, r)
            }
        }
        return e
    }

    function Pi(a, b, c) {
        var d = [], e = [];
        b[a] = Qi(d, e, c);
        return {
            onSuccess: function () {
                b[a] = Ri;
                for (var f = 0; f < d.length; f++) d[f]()
            }, onFailure: function () {
                b[a] = Si;
                for (var f = 0; f < e.length; f++) e[f]()
            }
        }
    }

    function Qi(a, b, c) {
        return function (d, e) {
            a.push(d);
            b.push(e);
            c()
        }
    }

    function Ri(a) {
        a()
    }

    function Si(a, b) {
        b()
    };var Vi = function (a, b) {
        for (var c = [], d = 0; d < Ub.length; d++) if (a[d]) {
            var e = Ub[d];
            var f = Yh(b.Oa);
            try {
                var h = Ni(d, {onSuccess: f, onFailure: f, terminate: f}, b, d);
                if (h) {
                    var k = c, l = k.push, p = d, q = e["function"];
                    if (!q) throw"Error: No function name given for function call.";
                    var n = Wb[q];
                    l.call(k, {Kf: p, Df: n ? n.priorityOverride || 0 : 0, th: h})
                } else Ti(d, b), f()
            } catch (r) {
                f()
            }
        }
        var u = b.Oa;
        u.Y = !0;
        u.o >= u.C && Wh(u);
        c.sort(Ui);
        for (var t = 0; t < c.length; t++) c[t].th();
        return 0 < c.length
    };

    function Ui(a, b) {
        var c, d = b.Df, e = a.Df;
        c = d > e ? 1 : d < e ? -1 : 0;
        var f;
        if (0 !== c) f = c; else {
            var h = a.Kf, k = b.Kf;
            f = h > k ? 1 : h < k ? -1 : 0
        }
        return f
    }

    function Ti(a, b) {
        if (!Ii) return;
        var c = function (d) {
            var e = b.Cd(Ub[d]) ? "3" : "4", f = ac(Ub[d][qb.af], b, []);
            f && f.length && c(f[0].index);
            Ki(b.id, Ub[d], e);
            var h = ac(Ub[d][qb.cf], b, []);
            h && h.length && c(h[0].index)
        };
        c(a);
    }

    var Wi = !1, aj = function (a) {
        var b = a["gtm.uniqueEventId"], c = a.event;
        if ("gtm.js" === c) {
            if (Wi) return !1;
            Wi = !0
        }
        var d = Eh(b), e = !1;
        if (!d.active) {
            if ("gtm.js" !== c) return !1;
            e = !0;
            d = Eh(Number.MAX_SAFE_INTEGER)
        }
        Ii && !zi[b] && vi !== b && (ri(), vi = b, Ci = xi = "", Gi[b] = "&e=" + (0 === c.indexOf("gtm.") ? encodeURIComponent(c) : "*") + "&eid=" + b, ui());
        var f = a.eventCallback, h = a.eventTimeout, k = {
            id: b, name: c, Cd: Ch(d.isAllowed), li: [], yf: function () {
                F(6)
            }, hf: Xi(b), Oa: new Th(f,
                h)
        };
        Yi(b, k.Oa);
        var l = ic(k);
        e && (l = Zi(l));
        var p = Vi(l, k);
        "gtm.js" !== c && "gtm.sync" !== c || ji(Je.D);
        switch (c) {
            case "gtm.init":
                p && F(20)
        }
        return $i(l, p)
    };

    function Xi(a) {
        return function (b) {
            Ii && (pb(b) || Mi(a, "input", b))
        }
    }

    function Yi(a, b) {
        ff(a, "event", 1);
        ff(a, "ecommerce", 1);
        ff(a, "gtm");
        ff(a, "eventModel");
    }

    function Zi(a) {
        for (var b = [], c = 0; c < a.length; c++) a[c] && Le[String(Ub[c][qb.Na])] && (b[c] = !0);
        return b
    }

    function $i(a, b) {
        if (!b) return b;
        for (var c = 0; c < a.length; c++) if (a[c] && Ub[c] && !Me[String(Ub[c][qb.Na])]) return !0;
        return !1
    }

    function bj(a, b) {
        if (a) {
            var c = "" + a;
            0 !== c.indexOf("http://") && 0 !== c.indexOf("https://") && (c = "https://" + c);
            "/" === c[c.length - 1] && (c = c.substring(0, c.length - 1));
            return me("" + c + b).href
        }
    }

    function cj(a, b) {
        return dj() ? bj(a, b) : void 0
    }

    function dj() {
        var a = !1;
        return a
    };var ej = function () {
        this.eventModel = {};
        this.targetConfig = {};
        this.containerConfig = {};
        this.remoteConfig = {};
        this.globalConfig = {};
        this.onSuccess = function () {
        };
        this.onFailure = function () {
        };
        this.setContainerTypeLoaded = function () {
        };
        this.getContainerTypeLoaded = function () {
        };
        this.eventId = void 0
    }, fj = function (a) {
        var b = new ej;
        b.eventModel = a;
        return b
    }, gj = function (a, b) {
        a.targetConfig = b;
        return a
    }, hj = function (a, b) {
        a.containerConfig = b;
        return a
    }, ij = function (a, b) {
        a.remoteConfig = b;
        return a
    }, jj = function (a, b) {
        a.globalConfig =
            b;
        return a
    }, kj = function (a, b) {
        a.onSuccess = b;
        return a
    }, lj = function (a, b) {
        a.setContainerTypeLoaded = b;
        return a
    }, mj = function (a, b) {
        a.getContainerTypeLoaded = b;
        return a
    }, nj = function (a, b) {
        a.onFailure = b;
        return a
    };
    ej.prototype.getWithConfig = function (a) {
        if (void 0 !== this.eventModel[a]) return this.eventModel[a];
        if (void 0 !== this.targetConfig[a]) return this.targetConfig[a];
        if (void 0 !== this.containerConfig[a]) return this.containerConfig[a];
        if (void 0 !== this.remoteConfig[a]) return this.remoteConfig[a];
        if (void 0 !== this.globalConfig[a]) return this.globalConfig[a]
    };
    var oj = function (a) {
        function b(e) {
            Oa(e, function (f) {
                c[f] = null
            })
        }

        var c = {};
        b(a.eventModel);
        b(a.targetConfig);
        b(a.containerConfig);
        b(a.globalConfig);
        var d = [];
        Oa(c, function (e) {
            d.push(e)
        });
        return d
    };
    var pj;
    if (3 === Je.nc.length) pj = "g"; else {
        var qj = "G";
        qj = "g";
        pj = qj
    }
    var rj = {"": "n", UA: "u", AW: "a", DC: "d", G: "e", GF: "f", HA: "h", GTM: pj, OPT: "o"}, sj = function (a) {
        var b = Je.D.split("-"), c = b[0].toUpperCase(), d = rj[c] || "i",
            e = a && "GTM" === c ? b[1] : "OPT" === c ? b[1] : "", f;
        if (3 === Je.nc.length) {
            var h = "w";
            h = eh() ? "s" : "o";
            f = "2" + h
        } else f = "";
        return f + d + Je.nc + e
    };
    var tj = function (a, b) {
        a.addEventListener && a.addEventListener.call(a, "message", b, !1)
    };
    var uj = function () {
        return Tc("iPhone") && !Tc("iPod") && !Tc("iPad")
    };
    Tc("Opera");
    Tc("Trident") || Tc("MSIE");
    Tc("Edge");
    !Tc("Gecko") || -1 != Qc.toLowerCase().indexOf("webkit") && !Tc("Edge") || Tc("Trident") || Tc("MSIE") || Tc("Edge");
    -1 != Qc.toLowerCase().indexOf("webkit") && !Tc("Edge") && Tc("Mobile");
    Tc("Macintosh");
    Tc("Windows");
    Tc("Linux") || Tc("CrOS");
    var vj = qa.navigator || null;
    vj && (vj.appVersion || "").indexOf("X11");
    Tc("Android");
    uj();
    Tc("iPad");
    Tc("iPod");
    uj() || Tc("iPad") || Tc("iPod");
    Qc.toLowerCase().indexOf("kaios");
    var wj = function (a, b) {
        for (var c = a, d = 0; 50 > d; ++d) {
            var e;
            try {
                e = !(!c.frames || !c.frames[b])
            } catch (k) {
                e = !1
            }
            if (e) return c;
            var f;
            a:{
                try {
                    var h = c.parent;
                    if (h && h != c) {
                        f = h;
                        break a
                    }
                } catch (k) {
                }
                f = null
            }
            if (!(c = f)) break
        }
        return null
    };
    var xj = function () {
    };
    var yj = function (a) {
        void 0 !== a.addtlConsent && "string" !== typeof a.addtlConsent && (a.addtlConsent = void 0);
        void 0 !== a.gdprApplies && "boolean" !== typeof a.gdprApplies && (a.gdprApplies = void 0);
        return void 0 !== a.tcString && "string" !== typeof a.tcString || void 0 !== a.listenerId && "number" !== typeof a.listenerId ? 2 : a.cmpStatus && "error" !== a.cmpStatus ? 0 : 3
    }, zj = function (a, b) {
        this.o = a;
        this.m = null;
        this.H = {};
        this.Y = 0;
        this.T = void 0 === b ? 500 : b;
        this.C = null
    };
    pa(zj, xj);
    var Bj = function (a) {
        return "function" === typeof a.o.__tcfapi || null != Aj(a)
    };
    zj.prototype.addEventListener = function (a) {
        var b = {}, c = Ic(function () {
            return a(b)
        }), d = 0;
        -1 !== this.T && (d = setTimeout(function () {
            b.tcString = "tcunavailable";
            b.internalErrorState = 1;
            c()
        }, this.T));
        var e = function (f, h) {
            clearTimeout(d);
            f ? (b = f, b.internalErrorState = yj(b), h && 0 === b.internalErrorState || (b.tcString = "tcunavailable", h || (b.internalErrorState = 3))) : (b.tcString = "tcunavailable", b.internalErrorState = 3);
            a(b)
        };
        try {
            Cj(this, "addEventListener", e)
        } catch (f) {
            b.tcString = "tcunavailable", b.internalErrorState = 3, d && (clearTimeout(d),
                d = 0), c()
        }
    };
    zj.prototype.removeEventListener = function (a) {
        a && a.listenerId && Cj(this, "removeEventListener", null, a.listenerId)
    };
    var Ej = function (a, b, c) {
        var d;
        d = void 0 === d ? "755" : d;
        var e;
        a:{
            if (a.publisher && a.publisher.restrictions) {
                var f = a.publisher.restrictions[b];
                if (void 0 !== f) {
                    e = f[void 0 === d ? "755" : d];
                    break a
                }
            }
            e = void 0
        }
        var h = e;
        if (0 === h) return !1;
        var k = c;
        2 === c ? (k = 0, 2 === h && (k = 1)) : 3 === c && (k = 1, 1 === h && (k = 0));
        var l;
        if (0 === k) if (a.purpose && a.vendor) {
            var p = Dj(a.vendor.consents, void 0 === d ? "755" : d);
            l = p && "1" === b && a.purposeOneTreatment && "DE" === a.publisherCC ? !0 : p && Dj(a.purpose.consents, b)
        } else l = !0; else l = 1 === k ? a.purpose && a.vendor ? Dj(a.purpose.legitimateInterests,
            b) && Dj(a.vendor.legitimateInterests, void 0 === d ? "755" : d) : !0 : !0;
        return l
    }, Dj = function (a, b) {
        return !(!a || !a[b])
    }, Cj = function (a, b, c, d) {
        c || (c = function () {
        });
        if ("function" === typeof a.o.__tcfapi) {
            var e = a.o.__tcfapi;
            e(b, 2, c, d)
        } else if (Aj(a)) {
            Fj(a);
            var f = ++a.Y;
            a.H[f] = c;
            if (a.m) {
                var h = {};
                a.m.postMessage((h.__tcfapiCall = {command: b, version: 2, callId: f, parameter: d}, h), "*")
            }
        } else c({}, !1)
    }, Aj = function (a) {
        if (a.m) return a.m;
        a.m = wj(a.o, "__tcfapiLocator");
        return a.m
    }, Fj = function (a) {
        a.C || (a.C = function (b) {
            try {
                var c;
                c = ("string" ===
                typeof b.data ? JSON.parse(b.data) : b.data).__tcfapiReturn;
                a.H[c.callId](c.returnValue, c.success)
            } catch (d) {
            }
        }, tj(a.o, a.C))
    };
    var Gj = !0;
    var Hj = {1: 0, 3: 0, 4: 0, 7: 3, 9: 3, 10: 3};

    function Ij(a, b) {
        if ("" === a) return b;
        var c = Number(a);
        return isNaN(c) ? b : c
    }

    var Jj = Ij("", 550), Kj = Ij("", 500);

    function Lj() {
        var a = L.tcf || {};
        return L.tcf = a
    }

    var Mj = function (a, b) {
        this.C = a;
        this.m = b;
        this.o = Za();
    }, Nj = function (a) {
    }, Oj = function (a) {
    }, Uj = function () {
        var a = Lj(), b = new zj(G, Gj ? 3E3 : -1), c = new Mj(b, a);
        if ((Pj() ? !0 === G.gtag_enable_tcf_support : !1 !== G.gtag_enable_tcf_support) && !a.active && ("function" === typeof G.__tcfapi || Bj(b))) {
            a.active = !0;
            a.Pb = {};
            Qj();
            var d = null;
            Gj ? d = G.setTimeout(function () {
                Rj(a);
                Sj(a);
                d = null
            }, Kj) : a.tcString = "tcunavailable";
            try {
                b.addEventListener(function (e) {
                    d && (clearTimeout(d), d = null);
                    if (0 !== e.internalErrorState) Rj(a), Sj(a), Nj(c);
                    else {
                        var f;
                        a.gdprApplies = e.gdprApplies;
                        if (!1 === e.gdprApplies) f = Tj(), b.removeEventListener(e); else if ("tcloaded" === e.eventStatus || "useractioncomplete" === e.eventStatus || "cmpuishown" === e.eventStatus) {
                            var h = {}, k;
                            for (k in Hj) if (Hj.hasOwnProperty(k)) if ("1" === k) {
                                var l = e, p = !0;
                                p = void 0 === p ? !1 : p;
                                var q;
                                var n = l;
                                !1 === n.gdprApplies ? q = !0 : (void 0 === n.internalErrorState && (n.internalErrorState = yj(n)), q = "error" === n.cmpStatus || 0 !== n.internalErrorState || "loaded" === n.cmpStatus && ("tcloaded" === n.eventStatus || "useractioncomplete" ===
                                    n.eventStatus) ? !0 : !1);
                                h["1"] = q ? !1 === l.gdprApplies || "tcunavailable" === l.tcString || void 0 === l.gdprApplies && !p || "string" !== typeof l.tcString || !l.tcString.length ? !0 : Ej(l, "1", 0) : !1
                            } else h[k] = Ej(e, k, Hj[k]);
                            f = h
                        }
                        f && (a.tcString = e.tcString || "tcempty", a.Pb = f, Sj(a), Nj(c))
                    }
                }), Oj(c)
            } catch (e) {
                d && (clearTimeout(d), d = null), Rj(a), Sj(a)
            }
        }
    };

    function Rj(a) {
        a.type = "e";
        a.tcString = "tcunavailable";
        Gj && (a.Pb = Tj())
    }

    function Qj() {
        var a = {};
        Ld((a.ad_storage = "denied", a.wait_for_update = Jj, a))
    }

    var Pj = function () {
        var a = !1;
        a = !0;
        return a
    };

    function Tj() {
        var a = {}, b;
        for (b in Hj) Hj.hasOwnProperty(b) && (a[b] = !0);
        return a
    }

    function Sj(a) {
        var b = {};
        Md((b.ad_storage = a.Pb["1"] ? "granted" : "denied", b))
    }

    var Vj = function () {
        var a = Lj();
        if (a.active && void 0 !== a.loadTime) return Number(a.loadTime)
    }, Wj = function () {
        var a = Lj();
        return a.active ? a.tcString || "" : ""
    }, Xj = function () {
        var a = Lj();
        return a.active && void 0 !== a.gdprApplies ? a.gdprApplies ? "1" : "0" : ""
    }, Yj = function (a) {
        if (!Hj.hasOwnProperty(String(a))) return !0;
        var b = Lj();
        return b.active && b.Pb ? !!b.Pb[String(a)] : !0
    };
    var Zj = !1;

    function ak(a) {
        var b = String(G.location).split(/[?#]/)[0], c = Je.Rf || G._CONSENT_MODE_SALT;
        return a ? c ? String(nf(b + a + c)) : "0" : ""
    }

    function bk(a) {
        function b(t) {
            var r;
            L.reported_gclid || (L.reported_gclid = {});
            r = L.reported_gclid;
            var v;
            v = Zj && h && (!Ed() || Nd(C.B)) ? l + "." + (f.prefix || "_gcl") + (t ? "gcu" : "gcs") : l + (t ? "gcu" : "gcs");
            if (!r[v]) {
                r[v] = !0;
                var w = [], y = function (D, E) {
                    E && w.push(D + "=" + encodeURIComponent(E))
                }, x = "https://www.google.com";
                if (Ed()) {
                    var A = Nd(C.B);
                    y("gcs", Od());
                    t && y("gcu", "1");
                    L.dedupe_gclid || (L.dedupe_gclid =
                        "" + Ef());
                    y("rnd", L.dedupe_gclid);
                    if ((!l || p && "aw.ds" !== p) && Nd(C.B)) {
                        var z = Bg("_gcl_aw");
                        y("gclaw", z.join("."))
                    }
                    y("url", String(G.location).split(/[?#]/)[0]);
                    y("dclid", ck(d, q));
                    !A && d && (x = "https://pagead2.googlesyndication.com")
                }
                y("gdpr_consent", Wj()), y("gdpr", Xj());
                "1" === lg(!1)._up && y("gtm_up", "1");
                y("gclid", ck(d,
                    l));
                y("gclsrc", p);
                y("gtm", sj(!e));
                Zj && h && Nd(C.B) && (Qf(f || {}), y("auid", Lf[Mf(f.prefix)] || ""));
                var B = x + "/pagead/landing?" + w.join("&");
                qd(B)
            }
        }

        var c = !!a.sd, d = !!a.ra, e = a.R, f = void 0 === a.uc ? {} : a.uc, h = void 0 === a.Bc ? !0 : a.Bc, k = Hg(),
            l = k.gclid || "", p = k.gclsrc, q = k.dclid || "", n = !c && (!l || p && "aw.ds" !== p ? !1 : !0),
            u = Ed();
        if (n || u) u ? Pd(function () {
            b();
            Nd(C.B) || Id(function (t) {
                    return b(!0, t.jf)
                },
                C.B)
        }, [C.B]) : b()
    }

    function ck(a, b) {
        var c = a && !Nd(C.B);
        return b && c ? "0" : b
    }

    var dk = function (a) {
        var b = cj(a, "/pagead/conversion_async.js");
        if (b) return b;
        var c = -1 !== navigator.userAgent.toLowerCase().indexOf("firefox"),
            d = gh("https://", "http://", "www.googleadservices.com");
        if (c || 1 === vh()) d = "https://www.google.com";
        return d + "/pagead/conversion_async.js"
    }, ek = !1, fk = [], gk = ["aw", "dc"], hk = function (a) {
        var b = G.google_trackConversion, c = a.gtm_onFailure;
        "function" == typeof b ? b(a) || c() : c()
    }, ik = function () {
        for (; 0 < fk.length;) hk(fk.shift())
    }, jk = function (a, b) {
        var c = !1;
        var d = ek;
        c && (d = b.getContainerTypeLoaded("AW"));
        if (!d) {
            ek = !0;
            Zh();
            var e = function () {
                c && b.setContainerTypeLoaded("AW", !0);
                ik();
                fk = {push: hk}
            };
            eh() ? e() : gd(a, e, function () {
                ik();
                ek = !1;
                c && b.setContainerTypeLoaded("AW", !1)
            })
        }
    }, kk = function (a) {
        if (a) {
            for (var b = [], c = 0; c < a.length; ++c) {
                var d = a[c];
                d && b.push({
                    item_id: d.id,
                    quantity: d.quantity,
                    value: d.price,
                    start_date: d.start_date,
                    end_date: d.end_date
                })
            }
            return b
        }
    }, lk = function (a, b, c, d) {
        function e() {
            Ca("gdpr_consent", Wj()), Ca("gdpr", Xj());
        }

        function f() {
            var la = [];
            return la
        }

        function h(la) {
            var ya = !0, Ea = [];
            if (la) {
                Ea = f();
            }
            t && (Y("delopc", r(C.gd)), Y("oedeld", r(C.Ee)), Y("delc",
                r(C.ve)), Y("shf", r(C.Be)), Y("iedeld", wh(r(C.V))));
            ya && fk.push(Q)
        }

        function k() {
            return function (la) {
                v && (la = uh(la));
                return la
            }
        }

        function l() {
        }

        var p = bh(a), q = b == C.Z;
        p.containerId !== Je.D && F(61);
        var n = p.F[0],
            u = p.F[1], t = void 0 !== u, r = function (la) {
                return d.getWithConfig(la)
            }, v = void 0 != r(C.M) && !1 !== r(C.M), w = !1 !== r(C.sb), y = r(C.rb) || r(C.aa), x = r(C.W), A = r(C.ja),
            z = r(C.ma), B = r(C.fg), D = jb(ob(B) ? B : {}), E = r(C.Da), I = dk(E);
        jk(I, d);
        var M = {prefix: y, domain: x, Nb: A, flags: z};
        if (q) {
            var N = r(C.ka) || {};
            if (w) {
                var V = r(C.tb), ia = void 0 === V ? !0 : !!V;
                tg(N[C.ab], !!N[C.I]) && Mg(gk, M);
                Kg(M);
                Og(["aw", "dc"], M);
            }
            r(C.Ea) && Qg();
            N[C.I] &&
            Ng(gk, N[C.I], N[C.eb], !!N[C.cb], y);
            rh(p, d);
            bk({sd: !1, ra: v, R: a, eventId: d.eventId, uc: w ? M : void 0, Bc: w, nf: D})
        }
        if (b === C.ya) {
            var O = r(C.Ba), K = r(C.Aa), R = r(O);
            if (O === C.Yb && void 0 === R) {
                var W = Wg("aw", M.prefix, v);
                0 === W.length ? K(void 0) : 1 === W.length ? K(W[0]) : K(W)
            } else K(R)
        } else {
            var ja = !1 === r(C.oe) || !1 === r(C.wb);
            if (!q || !t && !ja) if (!0 === r(C.pe) && (t = !1), !1 !== r(C.ia) || t) {
                var Q = {
                    google_conversion_id: n,
                    google_remarketing_only: !t,
                    onload_callback: d.onSuccess,
                    gtm_onFailure: d.onFailure,
                    google_conversion_format: "3",
                    google_conversion_color: "ffffff",
                    google_conversion_domain: "",
                    google_conversion_label: u,
                    google_conversion_language: r(C.$a),
                    google_conversion_value: r(C.Fa),
                    google_conversion_currency: r(C.za),
                    google_conversion_order_id: r(C.yb),
                    google_user_id: r(C.zb),
                    google_conversion_page_url: r(C.vb),
                    google_conversion_referrer_url: r(C.Ca),
                    google_gtm: sj()
                };
                Q.google_gtm_experiments = {capi: !0};
                t && (Q.google_transport_url = bj(E, "/"));
                Q.google_restricted_data_processing =
                    r(C.dd);
                eh() && (Q.opt_image_generator = function () {
                    return new Image
                }, Q.google_enable_display_cookie_match = !1);
                !1 === r(C.ia) && (Q.google_allow_ad_personalization_signals = !1);
                Q.google_read_gcl_cookie_opt_out = !w;
                w && y && (Q.google_gcl_cookie_prefix = y);
                var wa = function () {
                    var la = {event: b}, ya = d.eventModel;
                    if (!ya) return null;
                    m(ya, la);
                    for (var Ea = 0; Ea < C.$d.length; ++Ea) delete la[C.$d[Ea]];
                    return la
                }();
                wa && (Q.google_custom_params = wa);
                !t && r(C.V) && (Q.google_gtag_event_data = {items: r(C.V)});
                if (t && b == C.la) {
                    Q.google_conversion_merchant_id =
                        r(C.ue);
                    Q.google_basket_feed_country = r(C.se);
                    Q.google_basket_feed_language = r(C.te);
                    Q.google_basket_discount = r(C.qe);
                    Q.google_basket_transaction_type = b;
                    Q.google_disable_merchant_reported_conversions = !0 === r(C.ye);
                    eh() && (Q.google_disable_merchant_reported_conversions = !0);
                    var ma = kk(r(C.V));
                    ma && (Q.google_conversion_items = ma)
                }
                var Y = function (la, ya) {
                    void 0 != ya && "" !== ya && (Q.google_additional_conversion_params = Q.google_additional_conversion_params || {}, Q.google_additional_conversion_params[la] = ya)
                }, Ca = function (la,
                                  ya) {
                    void 0 != ya && "" !== ya && (Q.google_additional_params = Q.google_additional_params || {}, Q.google_additional_params[la] = ya)
                };
                "1" === lg(!1)._up && Y("gtm_up", "1");
                t && (Y("vdnc", r(C.Fe)), Y("vdltv", r(C.we)));
                e();
                var wb = vh();
                0 === wb ? Ca("dg", "c") : 1 === wb && Ca("dg", "e");
                Q.google_gtm_url_processor = k();
                (function () {
                    Ed() ? Pd(function () {
                        e();
                        var la = Nd(C.B);
                        if (t) Y("gcs", Od()), l(), la || E || !v || (Q.google_transport_url = "https://pagead2.googlesyndication.com/"), h(la); else if (la) {
                            h(la);
                            return
                        }
                        la || Id(function (ya) {
                            var Ea = ya.jf;
                            Q = m(Q);
                            Q.google_gtm_url_processor = k(Ea);
                            !E && Q.google_transport_url && delete Q.google_transport_url;
                            e();
                            t && (Y("gcs", Od()), l(), Y("gcu", "1"));
                            h(!0)
                        }, C.B)
                    }, [C.B]) : h(!0)
                })()
            }
        }
    };
    var mk = function (a) {
        return !(void 0 === a || null === a || 0 === (a + "").length)
    }, nk = function (a, b) {
        var c;
        if (2 === b.da) return a("ord", Ka(1E11, 1E13)), !0;
        if (3 === b.da) return a("ord", "1"), a("num", Ka(1E11, 1E13)), !0;
        if (4 === b.da) return mk(b.sessionId) && a("ord", b.sessionId), !0;
        if (5 === b.da) c = "1"; else if (6 === b.da) c = b.Qd; else return !1;
        mk(c) && a("qty", c);
        mk(b.vc) && a("cost", b.vc);
        mk(b.transactionId) && a("ord", b.transactionId);
        return !0
    }, pk = function (a, b, c) {
        function d(z, B, D) {
            u.hasOwnProperty(z) || (B = String(B), n.push(";" + z + "=" + (D ?
                B : ok(B))))
        }

        function e(z, B) {
            B && d(z, B)
        }

        var f = a.ud, h = a.Td, k = a.protocol, l = a.yd;
        k += h ? "//" + f + ".fls.doubleclick.net/activityi" : "//ad.doubleclick.net/activity";
        var p = ";", q = !Nd(C.B) && !l && a.ra;
        q && (k = "https://ade.googlesyndication.com/ddm/activity", p = "/", h = !1);
        var n = [p, "src=" + ok(f), ";type=" + ok(a.xd), ";cat=" + ok(a.Fb)], u = a.oh || {};
        Oa(u, function (z, B) {
            n.push(";" + ok(z) + "=" + ok(B + ""))
        });
        if (nk(d, a)) {
            mk(a.Nc) && d("u", a.Nc);
            mk(a.Mc) && d("tran", a.Mc);
            d("gtm", sj());
            Ed() && !l && (d("gcs", Od()), c && d("gcu", "1"));
            e("gdpr_consent", Wj()), e("gdpr", Xj());
            "1" === lg(!1)._up && d("gtm_up", "1");
            !1 === a.Ug && d("npa", "1");
            if (a.td) {
                var t = void 0 === a.ra ? !0 : !!a.ra, r = Wg("dc", a.gb, t);
                r && r.length && d("gcldc", r.join("."));
                var v = Wg("aw", a.gb, t);
                v && v.length && d("gclaw", v.join("."));
                var w = Xg(t);
                w && d("gac", w);
                Qf({prefix: a.gb, Nb: a.lh, domain: a.kh, flags: a.Gi});
                var y = Lf[Mf(a.gb)];
                y && d("auiddc", y)
            }
            mk(a.Ld) && d("prd", a.Ld, !0);
            Oa(a.Xd, function (z, B) {
                d(z, B)
            });
            n.push(b || "");
            if (mk(a.Dc)) {
                var x = a.Dc || "";
                q &&
                (x = ne(x));
                d("~oref", x)
            }
            var A = k + n.join("") + "?";
            h ? id(A, a.onSuccess) : jd(A, a.onSuccess, a.onFailure)
        } else J(a.onFailure)
    }, ok = encodeURIComponent, qk = function (a, b) {
        !Ed() || a.yd ? pk(a, b) : Pd(function () {
            pk(a, b);
            Nd(C.B) || Id(function () {
                pk(a, b, !0)
            }, C.B)
        }, [C.B])
    };
    var rk = function (a, b, c, d) {
        function e() {
            var f = {config: a, gtm: sj()};
            c && (Qf(d), f.auiddc = Lf[Mf(d.prefix)]);
            b && (f.loadInsecure = b);
            void 0 === G.__dc_ns_processor && (G.__dc_ns_processor = []);
            G.__dc_ns_processor.push(f);
            gd((b ? "http" : "https") + "://www.googletagmanager.com/dclk/ns/v1.js")
        }

        Nd(C.B) ? e() : Id(e, C.B)
    }, sk = function (a) {
        var b = /^u([1-9]\d?|100)$/, c = a.getWithConfig(C.xe) || {}, d = oj(a), e = {}, f = {};
        if (ob(c)) for (var h in c) if (c.hasOwnProperty(h) && b.test(h)) {
            var k = c[h];
            g(k) && (e[h] = k)
        }
        for (var l = 0; l < d.length; l++) {
            var p =
                d[l];
            b.test(p) && (e[p] = p)
        }
        for (var q in e) e.hasOwnProperty(q) && (f[q] = a.getWithConfig(e[q]));
        return f
    }, tk = function (a) {
        function b(l, p, q) {
            void 0 !== q && 0 !== (q + "").length && d.push(l + p + ":" + c(q + ""))
        }

        var c = encodeURIComponent, d = [], e = a(C.V) || [];
        if (Ha(e)) for (var f = 0; f < e.length; f++) {
            var h = e[f], k = f + 1;
            b("i", k, h.id);
            b("p", k, h.price);
            b("q", k, h.quantity);
            b("c", k, a(C.ve));
            b("l", k, a(C.$a))
        }
        return d.join("|")
    }, uk = function (a) {
        var b = /^DC-(\d+)(\/([\w-]+)\/([\w-]+)\+(\w+))?$/.exec(a);
        if (b) {
            var c = {
                standard: 2, unique: 3, per_session: 4,
                transactions: 5, items_sold: 6, "": 1
            }[(b[5] || "").toLowerCase()];
            if (c) return {containerId: "DC-" + b[1], R: b[3] ? a : "", Og: b[1], Ng: b[3] || "", Fb: b[4] || "", da: c}
        }
    }, xk = function (a, b, c, d) {
        var e = uk(a);
        if (e) {
            e.containerId !== Je.D && F(59);
            var f = function (K) {
                    return d.getWithConfig(K)
                }, h = !1 !== f(C.sb), k = f(C.rb) || f(C.aa), l = f(C.W), p = f(C.ja), q = f(C.ma), n = f(C.hg),
                u = void 0 != f(C.M) && !1 !== f(C.M), t = 3 === fh();
            if (b === C.ya) {
                var r = f(C.Ba), v = f(C.Aa), w = f(r);
                if (r === C.Yb && void 0 === w) {
                    var y = Wg("dc", k, u);
                    0 === y.length ? v(void 0) : 1 === y.length ? v(y[0]) :
                        v(y)
                } else v(w)
            } else if (b === C.Z) {
                var x = {prefix: k, domain: l, Nb: p, flags: q}, A = f(C.ka) || {}, z = f(C.tb),
                    B = void 0 === z ? !0 : !!z;
                h && (tg(A[C.ab], !!A[C.I]) && Mg(vk, x), Kg(x), Og(vk, x), wk ? Yg(B, x) : Zg(B, x));
                A[C.I] && Ng(vk, A[C.I], A[C.eb], !!A[C.cb], k);
                f(C.Ea) && Qg();
                if (n && n.exclusion_parameters && n.engines) if (eh()) {
                } else rk(n, t, h, x);
                bk({sd: !0, ra: u, R: a, eventId: d.eventId, uc: h ? x : void 0, Bc: h});
                J(d.onSuccess)
            } else {
                var D = {}, E = f(C.gg);
                if (ob(E)) for (var I in E) if (E.hasOwnProperty(I)) {
                    var M =
                        E[I];
                    void 0 !== M && null !== M && (D[I] = M)
                }
                var N = "";
                if (5 === e.da || 6 === e.da) N = tk(f);
                var V = sk(d), ia = !0 === f(C.cg);
                if (eh() && ia) {
                    ia = !1
                }
                var O = {
                    Fb: e.Fb,
                    td: h,
                    kh: l,
                    lh: p,
                    gb: k,
                    vc: f(C.Fa),
                    da: e.da,
                    oh: D,
                    ud: e.Og,
                    xd: e.Ng,
                    onFailure: d.onFailure,
                    onSuccess: d.onSuccess,
                    Dc: le(me(G.location.href)),
                    Ld: N,
                    protocol: t ? "http:" : "https:",
                    Qd: f(C.ug),
                    Td: ia,
                    sessionId: f(C.fc),
                    Mc: void 0,
                    transactionId: f(C.yb),
                    Nc: void 0,
                    Xd: V,
                    Ug: !1 !== f(C.ia),
                    eventId: d.eventId,
                    ra: u
                };
                qk(O)
            }
        } else J(d.onFailure)
    }, vk = ["aw", "dc"], wk = !1;
    var zk = function (a) {
        function b() {
            var d = c, e = yk(JSON.stringify(a[d])),
                f = "https://www.google.com/travel/flights/click/conversion/" + yk(a.conversion_id) + "/?" + d + "=" + e;
            if (a.conversionLinkerEnabled) {
                var h = Wg("gf", a.cookiePrefix, void 0);
                if (h && h.length) for (var k = 0; k < h.length; k++) f += "&gclgf=" + yk(h[k])
            }
            jd(f, a.onSuccess, a.onFailure)
        }

        var c;
        if (a.hasOwnProperty("conversion_data")) c = "conversion_data"; else if (a.hasOwnProperty("price")) c = "price"; else return;
        Nd(C.B) ? b() : Id(b, C.B)
    }, yk = function (a) {
        return null === a || void 0 ===
        a || 0 === String(a).length ? "" : encodeURIComponent(String(a))
    };
    var Ak = /.*\.google\.com(:\d+)?\/(?:booking|travel)\/flights.*/, Ck = function (a, b, c, d) {
        var e = function (E) {
            return d.getWithConfig(E)
        }, f = bh(a), h = f.F[0];
        f.containerId !== Je.D && F(62);
        var k = !1 !== e(C.sb), l = e(C.rb) || e(C.aa), p = e(C.W), q = e(C.ja), n = e(C.ma);
        if (b === C.ya) {
            var u = e(C.Ba), t = e(C.Aa), r = e(u);
            if (u === C.Yb && void 0 === r) {
                var v = void 0 != e(C.M) && !1 !== e(C.M), w = Wg("gf", l, v);
                0 === w.length ? t(void 0) : 1 === w.length ? t(w[0]) : t(w)
            } else t(r)
        } else if (b === C.Z) {
            if (k) {
                var y = {prefix: l, domain: p, flags: n, Nb: q};
                Kg(y);
                Og(["aw", "dc"],
                    y)
            }
            J(d.onSuccess)
        } else {
            var x = {
                conversion_id: h,
                onFailure: d.onFailure,
                onSuccess: d.onSuccess,
                conversionLinkerEnabled: k,
                cookiePrefix: l
            }, A = Ak.test(G.location.href);
            if (b === C.xa) {
                var z = {partner_id: h, is_direct_booking: A, total_price: e(C.Fa), currency: e(C.za)};
                x.price = z;
                zk(x)
            } else if (b !== C.la) J(d.onFailure); else {
                var B = {
                    partner_id: h,
                    trip_type: e(C.zg),
                    total_price: e(C.Fa),
                    currency: e(C.za),
                    is_direct_booking: A,
                    flight_segment: Bk(e(C.V))
                }, D = e(C.qg);
                D && "object" === typeof D && (B.passengers_total = Sa(D.total), B.passengers_adult =
                    Sa(D.adult), B.passengers_child = Sa(D.child), B.passengers_infant_in_seat = Sa(D.infant_in_seat), B.passengers_infant_in_lap = Sa(D.infant_in_lap));
                x.conversion_data = B;
                zk(x)
            }
        }
    }, Bk = function (a) {
        if (a) {
            for (var b = [], c = 0, d = 0; d < a.length; ++d) {
                var e = a[d];
                !e || void 0 !== e.category && "" !== e.category && "FlightSegment" !== e.category || (b[c] = {
                    cabin: e.travel_class,
                    fare_product: e.fare_product,
                    booking_code: e.booking_code,
                    flight_number: e.flight_number,
                    origin: e.origin,
                    destination: e.destination,
                    departure_date: e.start_date
                }, c++)
            }
            return b
        }
    };

    var Hk = function (a, b, c, d) {
        function e() {
            Wj() && (z += "&gdpr_consent=" + encodeURIComponent(Wj())), Xj() && (z += "&gdpr=" + encodeURIComponent(Xj()));
            if (k) {
                var E = b === C.xa ? Wg("aw", l, void 0) : Wg("ha", l, void 0);
                z += E.map(function (I) {
                    return "&gclha=" + encodeURIComponent(I)
                }).join("")
            }
            jd(z, d.onSuccess, d.onFailure)
        }

        var f = bh(a);
        f.containerId !== Je.D && F(63);
        var h = function (E) {
                return d.getWithConfig(E)
            }, k = !1 !== h(C.sb), l = h(C.rb) || h(C.aa), p = h(C.W),
            q = h(C.ja), n = h(C.ma);
        if (b === C.ya) {
            var u = h(C.Ba), t = h(C.Aa), r = h(u);
            if (u === C.Yb && void 0 === r) {
                var v = void 0 != h(C.M) && !1 !== h(C.M), w = Wg("ha", l, v);
                0 === w.length ? t(void 0) : 1 === w.length ? t(w[0]) : t(w)
            } else t(r)
        } else if (b === C.Z) {
            var y = h(C.ka) || {};
            if (k) {
                var x = {prefix: l, domain: p, flags: n, Nb: q};
                tg(y[C.ab], !!y[C.I]) && Mg(Dk, x);
                Kg(x);
                Og(["aw", "dc"], x)
            }
            y[C.I] && Ng(Dk, y[C.I], y[C.eb], !!y[C.cb], l);
            J(d.onSuccess)
        } else {
            var A = f.F[0];
            if (/^\d+$/.test(A)) {
                var z = "https://www.googletraveladservices.com/travel/clk/pagead/conversion/" +
                    encodeURIComponent(A) + "/";
                if (b === C.la) {
                    var B = Ek(h(C.yb), h(C.Fa), h(C.za), h(C.V));
                    B = encodeURIComponent(Fk(B));
                    z += "?data=" + B
                } else if (b === C.xa) {
                    var D = Gk(A, h(C.Fa), h(C.Ce), h(C.za), h(C.V));
                    D = encodeURIComponent(Fk(D));
                    z += "?label=FH&guid=ON&script=0&ord=" + Ka(0, 4294967295) + ("&price=" + D)
                } else {
                    J(d.onFailure);
                    return
                }
                Nd(C.B) ? e() : Id(e, C.B)
            } else J(d.onFailure)
        }
    }, Ek = function (a, b, c, d) {
        var e = {};
        Ik(a) && (e.hct_booking_xref = a);
        g(c) && (e.hct_currency_code = c);
        Ik(b) && (e.hct_total_price = b, e.hct_base_price = b);
        if (!Ha(d) || 0 ===
            d.length) return e;
        var f = d[0];
        if (!ob(f)) return e;
        Ik(f[Jk.ld]) && (e.hct_partner_hotel_id = f[Jk.ld]);
        g(f[Jk.nd]) && (e.hct_checkin_date = f[Jk.nd]);
        g(f[Jk.Qc]) && (e.hct_checkout_date = f[Jk.Qc]);
        return e
    }, Gk = function (a, b, c, d, e) {
        function f(q) {
            void 0 === q && (q = 0);
            if (Ik(q)) return l + q
        }

        function h(q, n, u) {
            u(n) && (k[q] = n)
        }

        var k = {};
        k.partner_id = a;
        var l = "USD";
        g(d) && (l = k.currency = d);
        Ik(b) && (k.base_price_value_string = f(b), k.display_price_value_string = f(b));
        Ik(c) && (k.tax_price_value_string = f(c));
        g("LANDING_PAGE") && (k.page_type =
            "LANDING_PAGE");
        if (!Ha(e) || 0 == e.length) return k;
        var p = e[0];
        if (!ob(p)) return k;
        Ik(p[Jk.Ye]) && (k.total_price_value_string = f(p[Jk.Ye]));
        h("partner_hotel_id", p[Jk.ld], Ik);
        h("check_in_date", p[Jk.nd], g);
        h("check_out_date", p[Jk.Qc], g);
        h("adults", p[Jk.Ig], Kk);
        h(Jk.$e, p[Jk.$e], g);
        h(Jk.Ze, p[Jk.Ze], g);
        return k
    }, Fk = function (a) {
        var b = [];
        Oa(a, function (c, d) {
            b.push(c + "=" + d)
        });
        return b.join(";")
    }, Ik = function (a) {
        return g(a) || Kk(a)
    }, Kk = function (a) {
        return "number" === typeof a
    }, Jk = {
        ld: "id", Ye: "price", nd: "start_date", Qc: "end_date",
        Ig: "occupancy", $e: "room_id", Ze: "rate_rule_id"
    }, Dk = ["ha"];
    var Mk = function () {
        var a = !0;
        Yj(7) && Yj(9) && Yj(10) || (a = !1);
        var b = !0;
        b = !1;
        b && !Lk() && (a = !1);
        return a
    }, Lk = function () {
        var a = !0;
        Yj(3) && Yj(4) || (a = !1);
        return a
    };
    var Qk = function (a, b) {
            var c = b.getWithConfig(C.Ba), d = b.getWithConfig(C.Aa), e = b.getWithConfig(c);
            if (void 0 === e) {
                var f = void 0;
                Nk.hasOwnProperty(c) ? f = Nk[c] : Ok.hasOwnProperty(c) && (f = Ok[c]);
                1 === f && (f = Pk(c));
                g(f) ? di()(function () {
                    var h = di().getByName(a).get(f);
                    d(h)
                }) : d(void 0)
            } else d(e)
        }, Tk = function (a, b, c) {
            if (Ed()) {
                var d = function () {
                    var e = di(), f = Rk(a, b, "", c);
                    Sk(b, f.Ga) && (e(function () {
                        e.remove(b)
                    }), e("create", a, f.Ga))
                };
                Id(d, C.J);
                Id(d, C.B)
            }
        }, $k = function (a, b, c) {
            var d = "https://www.google-analytics.com/analytics.js",
                e = fi();
            if (Da(e)) {
                var f = "gtag_" + a.split("-").join("_"), h = function (x) {
                    var A = [].slice.call(arguments, 0);
                    A[0] = f + "." + A[0];
                    e.apply(window, A)
                }, k = function () {
                    var x = function (D, E) {
                        for (var I = 0; E && I < E.length; I++) h(D, E[I])
                    }, A = Uk(b, c);
                    if (A) {
                        var z = A.action;
                        if ("impressions" === z) x("ec:addImpression", A.Dh); else if ("promo_click" === z || "promo_view" === z) {
                            var B = A.Nd;
                            x("ec:addPromo", A.Nd);
                            B && 0 < B.length && "promo_click" === z && h("ec:setAction", z)
                        } else x("ec:addProduct", A.jb), h("ec:setAction", z, A.Eb)
                    }
                }, l = function () {
                    if (eh()) {
                    } else {
                        var x = c.getWithConfig(C.pg);
                        x && (h("require", x, {dataLayer: "dataLayer"}), h("require", "render"))
                    }
                }, p = Rk(a, f, b, c), q = function (x, A, z) {
                    z && (A = "" + A);
                    p.Ha[x] = A
                };
                Sk(f, p.Ga) && (e(function () {
                    di() && di().remove(f)
                }), Vk[f] = !1);
                e("create", a, p.Ga);
                if (p.Ga._x_19) {
                    var n = cj(p.Ga._x_19, "/analytics.js");
                    n && (d = n);
                    p.Ga._x_20 && !Vk[f] && (Vk[f] = !0, e(ki(f, p.Ga._x_20)))
                }
                (function () {
                    var x = c.getWithConfig("custom_map");
                    e(function () {
                        if (ob(x)) {
                            var A = p.Ha, z = di().getByName(f), B;
                            for (B in x) if (x.hasOwnProperty(B) &&
                                /^(dimension|metric)\d+$/.test(B) && void 0 != x[B]) {
                                var D = z.get(Pk(x[B]));
                                Wk(A, B, D)
                            }
                        }
                    })
                })();
                (function (x) {
                    if (x) {
                        var A = {};
                        if (ob(x)) for (var z in Xk) Xk.hasOwnProperty(z) && Yk(Xk[z], z, x[z], A);
                        h("require", "linkid", A)
                    }
                })(p.linkAttribution);
                var u = p[C.ka];
                if (u && u[C.I]) {
                    var t = u[C.eb];
                    gi(f + ".", u[C.I], void 0 === t ? !!u.use_anchor : "fragment" === t, !!u[C.cb])
                }
                b === C.Sc ? (l(), h("send", "pageview", p.Ha)) : b === C.Z ? (l(), rh(a, c), c.getWithConfig(C.Ea) && (Qg(), hi(f + ".")), 0 != p.sendPageView && h("send", "pageview", p.Ha), Tk(a, f, c)) : b ===
                C.ya ? Qk(f, c) : "screen_view" === b ? h("send", "screenview", p.Ha) : "timing_complete" === b ? (q("timingCategory", p.eventCategory, !0), q("timingVar", p.name, !0), q("timingValue", Sa(p.value)), void 0 !== p.eventLabel && q("timingLabel", p.eventLabel, !0), h("send", "timing", p.Ha)) : "exception" === b ? h("send", "exception", p.Ha) : "optimize.callback" !== b && (0 <= Ia([C.Xb, "select_content", C.xa, C.Wa, C.Xa, C.Ka, "set_checkout_option", C.la, C.Ya, "view_promotion", "checkout_progress"], b) && (h("require", "ec", "ec.js"), k()), q("eventCategory", p.eventCategory,
                    !0), q("eventAction", p.eventAction || b, !0), void 0 !== p.eventLabel && q("eventLabel", p.eventLabel, !0), void 0 !== p.value && q("eventValue", Sa(p.value)), h("send", "event", p.Ha));
                var r = !1;
                var v = Zk;
                r && (v = c.getContainerTypeLoaded("UA"));
                if (!v) {
                    Zk = !0;
                    r && c.setContainerTypeLoaded("UA", !0);
                    Zh();
                    var w = function () {
                        r && c.setContainerTypeLoaded("UA", !1);
                        c.onFailure()
                    }, y =
                        function () {
                            di().loaded || w()
                        };
                    eh() ? J(y) : gd(d, y, w)
                }
            } else J(c.onFailure)
        }, al = function (a, b, c, d) {
            Pd(function () {
                $k(a, b, d)
            }, [C.J, C.B])
        }, bl = function (a) {
            return Nd(a)
        }, Zk, Vk = {}, Nk = {
            client_id: 1,
            client_storage: "storage",
            cookie_name: 1,
            cookie_domain: 1,
            cookie_expires: 1,
            cookie_path: 1,
            cookie_update: 1,
            cookie_flags: 1,
            sample_rate: 1,
            site_speed_sample_rate: 1,
            use_amp_client_id: 1,
            store_gac: 1,
            conversion_linker: "storeGac"
        }, Ok = {
            anonymize_ip: 1,
            app_id: 1,
            app_installer_id: 1,
            app_name: 1,
            app_version: 1,
            campaign: {
                name: "campaignName",
                source: "campaignSource",
                medium: "campaignMedium",
                term: "campaignKeyword",
                content: "campaignContent",
                id: "campaignId"
            },
            currency: "currencyCode",
            description: "exDescription",
            fatal: "exFatal",
            language: 1,
            non_interaction: 1,
            page_hostname: "hostname",
            page_referrer: "referrer",
            page_path: "page",
            page_location: "location",
            page_title: "title",
            screen_name: 1,
            transport_type: "transport",
            user_id: 1
        }, cl = {
            content_id: 1,
            event_category: 1,
            event_action: 1,
            event_label: 1,
            link_attribution: 1,
            linker: 1,
            method: 1,
            name: 1,
            send_page_view: 1,
            value: 1
        },
        Xk = {cookie_name: 1, cookie_expires: "duration", levels: 1}, dl = {
            anonymize_ip: 1,
            fatal: 1,
            non_interaction: 1,
            use_amp_client_id: 1,
            send_page_view: 1,
            store_gac: 1,
            conversion_linker: 1
        }, Yk = function (a, b, c, d) {
            if (void 0 !== c) if (dl[b] && (c = Wa(c)), "anonymize_ip" !== b || c || (c = void 0), 1 === a) d[Pk(b)] = c; else if (g(a)) d[a] = c; else for (var e in a) a.hasOwnProperty(e) && void 0 !== c[e] && (d[a[e]] = c[e])
        }, Pk = function (a) {
            return a && g(a) ? a.replace(/(_[a-z])/g, function (b) {
                return b[1].toUpperCase()
            }) : a
        }, el = function (a) {
            var b = "general";
            0 <= Ia([C.ie,
                C.Wa, C.me, C.Ka, "checkout_progress", C.la, C.Ya, C.Xa, "set_checkout_option"], a) ? b = "ecommerce" : 0 <= Ia("generate_lead login search select_content share sign_up view_item view_item_list view_promotion view_search_results".split(" "), a) ? b = "engagement" : "exception" === a && (b = "error");
            return b
        }, Wk = function (a, b, c) {
            a.hasOwnProperty(b) || (a[b] = c)
        }, fl = function (a) {
            if (Ha(a)) {
                for (var b = [], c = 0; c < a.length; c++) {
                    var d = a[c];
                    if (void 0 != d) {
                        var e = d.id, f = d.variant;
                        void 0 != e && void 0 != f && b.push(String(e) + "." + String(f))
                    }
                }
                return 0 <
                b.length ? b.join("!") : void 0
            }
        }, Rk = function (a, b, c, d) {
            function e(E, I) {
                void 0 !== I && (k[E] = I)
            }

            a !== Je.D && F(60);
            var f = function (E) {
                return d.getWithConfig(E)
            }, h = {}, k = {}, l = {}, p = fl(f(C.lg));
            p && Wk(k, "exp", p);
            Ed() && (l._cs = bl);
            var q = f("custom_map");
            if (ob(q)) for (var n in q) if (q.hasOwnProperty(n) && /^(dimension|metric)\d+$/.test(n) && void 0 != q[n]) {
                var u = f(String(q[n]));
                void 0 !== u && Wk(k, n, u)
            }
            for (var t = oj(d), r = 0; r < t.length; ++r) {
                var v = t[r], w = f(v);
                if (cl.hasOwnProperty(v)) Yk(cl[v], v, w, h); else if (Ok.hasOwnProperty(v)) Yk(Ok[v],
                    v, w, k); else if (Nk.hasOwnProperty(v)) Yk(Nk[v], v, w, l); else if (/^(dimension|metric|content_group)\d+$/.test(v)) Yk(1, v, w, k); else if ("developer_id" === v) {
                    var y = jb(w);
                    y && (k["&did"] = y)
                } else v === C.aa && 0 > Ia(t, C.Zb) && (l.cookieName = w + "_ga")
            }
            Wk(l, "cookieDomain", "auto");
            Wk(k, "forceSSL", !0);
            Wk(h, "eventCategory", el(c));
            0 <= Ia(["view_item", "view_item_list", "view_promotion", "view_search_results"], c) && Wk(k, "nonInteraction", !0);
            "login" === c || "sign_up" === c || "share" === c ? Wk(h, "eventLabel", f(C.og)) : "search" === c || "view_search_results" ===
            c ? Wk(h, "eventLabel", f(C.xg)) : "select_content" === c && Wk(h, "eventLabel", f(C.eg));
            var x = h[C.ka] || {}, A = x[C.ab];
            A || 0 != A && x[C.I] ? l.allowLinker = !0 : !1 === A && Wk(l, "useAmpClientId", !1);
            f(C.Ea) && (l._useUp = !0);
            !1 !== f(C.dg) && !1 !== f(C.qb) && Mk() || (k.allowAdFeatures = !1);
            if (!1 === f(C.ia) || !Lk()) {
                var z = "allowAdFeatures";
                z = "allowAdPersonalizationSignals";
                k[z] = !1
            }
            l.name = b;
            k["&gtm"] = sj(!0);
            k.hitCallback = d.onSuccess;
            Ed() && (k["&gcs"] = Od(), Nd(C.J) || (l.storage = "none"), Nd(C.B) || (k.allowAdFeatures = !1, l.storeGac = !1));
            var B = f(C.Da) || f(C.ng) || bf("gtag.remote_config." + a + ".url", 2),
                D = f(C.mg) || bf("gtag.remote_config." + a + ".dualId", 2);
            if (B && null != bd) {
                l._x_19 = B;
            }
            D && (l._x_20 = D);
            h.Ha = k;
            h.Ga = l;
            return h
        },
        Uk = function (a, b) {
            function c(v) {
                function w(x, A) {
                    for (var z = 0; z < A.length; z++) {
                        var B = A[z];
                        if (v[B]) {
                            y[x] = v[B];
                            break
                        }
                    }
                }

                var y = m(v);
                w("listPosition", ["list_position"]);
                w("creative", ["creative_name"]);
                w("list", ["list_name"]);
                w("position", ["list_position", "creative_slot"]);
                return y
            }

            function d(v) {
                for (var w = [], y = 0; v && y < v.length; y++) v[y] && w.push(c(v[y]));
                return w.length ? w : void 0
            }

            function e(v) {
                return {
                    id: f(C.yb),
                    affiliation: f(C.ig),
                    revenue: f(C.Fa),
                    tax: f(C.Ce),
                    shipping: f(C.Be),
                    coupon: f(C.jg),
                    list: f(C.Vc) || v
                }
            }

            for (var f = function (v) {
                return b.getWithConfig(v)
            }, h = f(C.V), k, l = 0; h && l < h.length && !(k = h[l][C.Vc]); l++) ;
            var p = f("custom_map");
            if (ob(p)) for (var q =
                0; h && q < h.length; ++q) {
                var n = h[q], u;
                for (u in p) p.hasOwnProperty(u) && /^(dimension|metric)\d+$/.test(u) && void 0 != p[u] && Wk(n, u, n[p[u]])
            }
            var t = null, r = f(C.kg);
            a === C.la || a === C.Ya ? t = {action: a, Eb: e(), jb: d(h)} : a === C.Wa ? t = {
                action: "add",
                jb: d(h)
            } : a === C.Xa ? t = {action: "remove", jb: d(h)} : a === C.xa ? t = {
                action: "detail",
                Eb: e(k),
                jb: d(h)
            } : a === C.Xb ? t = {action: "impressions", Dh: d(h)} : "view_promotion" === a ? t = {
                action: "promo_view",
                Nd: d(r)
            } : "select_content" === a && r && 0 < r.length ? t = {
                action: "promo_click",
                Nd: d(r)
            } : "select_content" === a ? t =
                {
                    action: "click",
                    Eb: {list: f(C.Vc) || k},
                    jb: d(h)
                } : a === C.Ka || "checkout_progress" === a ? t = {
                action: "checkout",
                jb: d(h),
                Eb: {step: a === C.Ka ? 1 : f(C.Ae), option: f(C.ze)}
            } : "set_checkout_option" === a && (t = {action: "checkout_option", Eb: {step: f(C.Ae), option: f(C.ze)}});
            t && (t.Hi = f(C.za));
            return t
        }, gl = {}, Sk = function (a, b) {
            var c = gl[a];
            gl[a] = m(b);
            if (!c) return !1;
            for (var d in b) if (b.hasOwnProperty(d) && b[d] !== c[d]) return !0;
            for (var e in c) if (c.hasOwnProperty(e) && c[e] !== b[e]) return !0;
            return !1
        };
    var hl = !1;

    function il() {
        var a = L;
        return a.gcq = a.gcq || new jl
    }

    var kl = function (a, b, c) {
        il().register(a, b, c)
    }, ll = function (a, b, c, d) {
        il().push("event", [b, a], c, d)
    }, ml = function (a, b) {
        il().push("config", [a], b)
    }, nl = function (a, b, c, d) {
        il().push("get", [a, b], c, d)
    }, ol = {}, pl = function () {
        this.status = 1;
        this.containerConfig = {};
        this.targetConfig = {};
        this.remoteConfig = {};
        this.o = null;
        this.m = !1
    }, ql = function (a, b, c, d, e) {
        this.type = a;
        this.C = b;
        this.R = c || "";
        this.m = d;
        this.o = e
    }, jl = function () {
        this.H = {};
        this.o = {};
        this.m = [];
        this.C = {AW: !1, UA: !1}
    }, rl = function (a, b) {
        var c = bh(b);
        return a.H[c.containerId] =
            a.H[c.containerId] || new pl
    }, sl = function (a, b, c) {
        if (b) {
            var d = bh(b);
            if (d && 1 === rl(a, b).status) {
                rl(a, b).status = 2;
                var e = {};
                Ii && (e.timeoutId = G.setTimeout(function () {
                    F(38);
                    ui()
                }, 3E3));
                a.push("require", [e], d.containerId);
                ol[d.containerId] = Za();
                if (eh()) {
                } else {
                    var h =
                        "/gtag/js?id=" + encodeURIComponent(d.containerId) + "&l=dataLayer&cx=c",
                        k = ("http:" != G.location.protocol ? "https:" : "http:") + ("//www.googletagmanager.com" + h),
                        l = cj(c, h) || k;
                    gd(l)
                }
            }
        }
    }, tl = function (a, b, c, d) {
        if (d.R) {
            var e = rl(a, d.R), f = e.o;
            if (f) {
                var h = m(c), k = m(e.targetConfig[d.R]), l = m(e.containerConfig), p = m(e.remoteConfig), q = m(a.o),
                    n = bf("gtm.uniqueEventId"), u = bh(d.R).prefix,
                    t = mj(lj(nj(kj(jj(ij(hj(gj(fj(h), k), l), p), q), function () {
                        Li(n, u, "2");
                    }), function () {
                        Li(n, u, "3");
                    }), function (r, v) {
                        a.C[r] = v
                    }), function (r) {
                        return a.C[r]
                    });
                try {
                    Li(n, u, "1");
                    f(d.R, b, d.C, t)
                } catch (r) {
                    Li(n, u, "4");
                }
            }
        }
    };
    aa = jl.prototype;
    aa.register = function (a, b, c) {
        var d = rl(this, a);
        if (3 !== d.status) {
            d.o = b;
            d.status = 3;
            if (c) {
                m(d.remoteConfig, c);
                d.remoteConfig = c
            }
            var e = bh(a), f = ol[e.containerId];
            if (void 0 !== f) {
                var h = L[e.containerId].bootstrap, k = e.prefix.toUpperCase();
                L[e.containerId]._spx && (k = k.toLowerCase());
                var l = bf("gtm.uniqueEventId"), p = k, q = Za() - h;
                if (Ii && !zi[l]) {
                    l !== vi && (ri(), vi = l);
                    var n = p + "." + Math.floor(h - f) +
                        "." + Math.floor(q);
                    Di = Di ? Di + "," + n : "&cl=" + n
                }
                delete ol[e.containerId]
            }
            this.flush()
        }
    };
    aa.push = function (a, b, c, d) {
        var e = Math.floor(Za() / 1E3);
        sl(this, c, b[0][C.Da] || this.o[C.Da]);
        hl && c && rl(this, c).m && (d = !1);
        this.m.push(new ql(a, e, c, b, d));
        d || this.flush()
    };
    aa.insert = function (a, b, c) {
        var d = Math.floor(Za() / 1E3);
        0 < this.m.length ? this.m.splice(1, 0, new ql(a, d, c, b, !1)) : this.m.push(new ql(a, d, c, b, !1))
    };
    aa.flush = function (a) {
        for (var b = this, c = [], d = !1; this.m.length;) {
            var e = this.m[0];
            if (e.o) hl ? !e.R || rl(this, e.R).m ? (e.o = !1, this.m.push(e)) : c.push(e) : (e.o = !1, this.m.push(e)); else switch (e.type) {
                case "require":
                    if (3 !== rl(this, e.R).status && !a) {
                        hl && this.m.push.apply(this.m, c);
                        return
                    }
                    Ii && G.clearTimeout(e.m[0].timeoutId);
                    break;
                case "set":
                    Oa(e.m[0], function (u, t) {
                        m(ib(u, t), b.o)
                    });
                    break;
                case "config":
                    var f = e.m[0], h = !!f[C.hc];
                    delete f[C.hc];
                    var k = rl(this, e.R), l = bh(e.R), p = l.containerId === l.id;
                    h || (p ? k.containerConfig =
                        {} : k.targetConfig[e.R] = {});
                    k.m && h || tl(this, C.Z, f, e);
                    k.m = !0;
                    delete f[C.Ab];
                    p ? m(f, k.containerConfig) : m(f, k.targetConfig[e.R]);
                    hl && (d = !0);
                    break;
                case "event":
                    tl(this, e.m[1], e.m[0], e);
                    break;
                case "get":
                    var q = {}, n = (q[C.Ba] = e.m[0], q[C.Aa] = e.m[1], q);
                    tl(this, C.ya, n, e)
            }
            this.m.shift()
        }
        hl && (this.m.push.apply(this.m, c), d && this.flush())
    };
    aa.getRemoteConfig = function (a) {
        return rl(this, a).remoteConfig
    };
    var ul = function (a, b, c) {
        function d(f, h) {
            var k = f[h];
            k = rd(f, h);
            return k
        }

        var e = {
            event: b,
            "gtm.element": a,
            "gtm.elementClasses": d(a, "className"),
            "gtm.elementId": a["for"] || md(a, "id") || "",
            "gtm.elementTarget": a.formTarget || d(a, "target") || ""
        };
        c && (e["gtm.triggers"] = c.join(","));
        e["gtm.elementUrl"] = (a.attributes && a.attributes.formaction ? a.formAction : "") || a.action || d(a, "href") || a.src || a.code || a.codebase ||
            "";
        return e
    }, vl = function (a) {
        L.hasOwnProperty("autoEventsSettings") || (L.autoEventsSettings = {});
        var b = L.autoEventsSettings;
        b.hasOwnProperty(a) || (b[a] = {});
        return b[a]
    }, wl = function (a, b, c) {
        vl(a)[b] = c
    }, xl = function (a, b, c, d) {
        var e = vl(a), f = $a(e, b, d);
        e[b] = c(f)
    }, yl = function (a, b, c) {
        var d = vl(a);
        return $a(d, b, c)
    };
    var zl = !!G.MutationObserver, Al = void 0, Bl = function (a) {
        if (!Al) {
            var b = function () {
                var c = H.body;
                if (c) if (zl) (new MutationObserver(function () {
                    for (var e = 0; e < Al.length; e++) J(Al[e])
                })).observe(c, {childList: !0, subtree: !0}); else {
                    var d = !1;
                    kd(c, "DOMNodeInserted", function () {
                        d || (d = !0, J(function () {
                            d = !1;
                            for (var e = 0; e < Al.length; e++) J(Al[e])
                        }))
                    })
                }
            };
            Al = [];
            H.body ? b() : J(b)
        }
        Al.push(a)
    };
    var Dl = !1, El = [];

    function Fl() {
        if (!Dl) {
            Dl = !0;
            for (var a = 0; a < El.length; a++) J(El[a])
        }
    }

    var Gl = function (a) {
        Dl ? J(a) : El.push(a)
    };
    var Hl = "HA GF G UA AW DC".split(" "), Il = !1, Jl = {}, Kl = !1;

    function Ll(a, b) {
        var c = {event: a};
        b && (c.eventModel = m(b), b[C.Wc] && (c.eventCallback = b[C.Wc]), b[C.$b] && (c.eventTimeout = b[C.$b]));
        return c
    }

    function Ml() {
        Il = Il || !L.gtagRegistered, L.gtagRegistered = !0, Il && (L.addTargetToGroup = function (a) {
            Nl(a, "default")
        });
        return Il
    }

    var Ol = function (a) {
        Oa(Jl, function (b, c) {
            var d = Ia(c, a);
            0 <= d && c.splice(d, 1)
        })
    }, Nl = function (a, b) {
        b = b.toString().split(",");
        for (var c = 0; c < b.length; c++) Jl[b[c]] = Jl[b[c]] || [], Jl[b[c]].push(a)
    };
    var Pl = {
        config: function (a) {
            var b;
            if (2 > a.length || !g(a[1])) return;
            var c = {};
            if (2 < a.length) {
                if (void 0 != a[2] && !ob(a[2]) || 3 < a.length) return;
                c = a[2]
            }
            var d = bh(a[1]);
            if (!d) return;
            Ol(d.id);
            Nl(d.id, c[C.$c] || "default");
            delete c[C.$c];
            Kl || F(43);
            We();
            if (Ml() && -1 !== Ia(Hl, d.prefix)) {
                "G" === d.prefix && (c[C.Ab] = !0);
                ml(c, d.id);
                return
            }
            ef("gtag.targets." + d.id, void 0);
            ef("gtag.targets." + d.id, m(c));
            var e = {};
            e[C.Ma] = d.id;
            b = Ll(C.Z, e);
            return b
        }, consent: function (a) {
            function b() {
                Ml() &&
                m(a[2], {subcommand: a[1]})
            }

            if (3 === a.length) {
                F(39);
                var c = We(), d = a[1];
                "default" === d ? (b(), Ld(a[2])) : "update" === d && (b(), Md(a[2], c))
            }
        }, event: function (a) {
            var b = a[1];
            if (!(2 > a.length) && g(b)) {
                var c;
                if (2 < a.length) {
                    if (!ob(a[2]) && void 0 != a[2] || 3 < a.length) return;
                    c = a[2]
                }
                var d = Ll(b, c);
                var e;
                var f = c && c[C.Ma];
                void 0 === f && (f = bf(C.Ma, 2), void 0 === f && (f = "default"));
                if (g(f) || Ha(f)) {
                    for (var h = f.toString().replace(/\s+/g, "").split(","), k = [], l = 0; l < h.length; l++) 0 <= h[l].indexOf("-") ? k.push(h[l]) :
                        k = k.concat(Jl[h[l]] || []);
                    e = dh(k)
                } else e = void 0;
                var p = e;
                if (!p) return;
                var q = Ml();
                We();
                for (var n = [], u = 0; q && u < p.length; u++) {
                    var t = p[u];
                    if (-1 !== Ia(Hl, t.prefix)) {
                        var r = m(c);
                        "G" === t.prefix && (r[C.Ab] = !0);
                        ll(b, r, t.id)
                    }
                    n.push(t.id)
                }
                d.eventModel = d.eventModel || {};
                0 < p.length ? d.eventModel[C.Ma] = n.join() : delete d.eventModel[C.Ma];
                Kl || F(43);
                return d
            }
        }, get: function (a) {
            F(53);
            if (4 !== a.length || !g(a[1]) || !g(a[2]) || !Da(a[3])) return;
            var b = bh(a[1]), c = String(a[2]),
                d = a[3];
            if (!b) return;
            Kl || F(43);
            if (!Ml() || -1 === Ia(Hl, b.prefix)) return;
            We();
            var e = {};
            Gh(m((e[C.Ba] = c, e[C.Aa] = d, e)));
            nl(c, function (f) {
                J(function () {
                    return d(f)
                })
            }, b.id);
        }, js: function (a) {
            if (2 == a.length && a[1].getTime) return Kl = !0, Ml(), {event: "gtm.js", "gtm.start": a[1].getTime()}
        }, policy: function () {
        }, set: function (a) {
            var b;
            2 == a.length && ob(a[1]) ? b = m(a[1]) : 3 == a.length && g(a[1]) && (b = {}, ob(a[2]) || Ha(a[2]) ? b[a[1]] = m(a[2]) : b[a[1]] = a[2]);
            if (b) {
                if (We(), Ml()) {
                    m(b);
                    var c = m(b);
                    il().push("set", [c])
                }
                b._clear = !0;
                return b
            }
        }
    }, Ql = {policy: !0};
    var Rl = function (a, b) {
        var c = a.hide;
        if (c && void 0 !== c[b] && c.end) {
            c[b] = !1;
            var d = !0, e;
            for (e in c) if (c.hasOwnProperty(e) && !0 === c[e]) {
                d = !1;
                break
            }
            d && (c.end(), c.end = null)
        }
    }, Tl = function (a) {
        var b = Sl(), c = b && b.hide;
        c && c.end && (c[a] = !0)
    };
    var lm = function (a) {
        if (km(a)) return a;
        this.m = a
    };
    lm.prototype.Ah = function () {
        return this.m
    };
    var km = function (a) {
        return !a || "object" !== lb(a) || ob(a) ? !1 : "getUntrustedUpdateValue" in a
    };
    lm.prototype.getUntrustedUpdateValue = lm.prototype.Ah;
    var mm = [], nm = !1, om = !1, pm = !1, qm = function (a) {
        return G["dataLayer"].push(a)
    }, rm = function (a) {
        var b = L["dataLayer"], c = b ? b.subscribers : 1, d = 0;
        return function () {
            ++d === c && a()
        }
    };

    function sm(a) {
        var b = a._clear;
        Oa(a, function (d, e) {
            "_clear" !== d && (b && ef(d, void 0), ef(d, e))
        });
        Re || (Re = a["gtm.start"]);
        var c = a["gtm.uniqueEventId"];
        if (!a.event) return !1;
        c || (c = We(), a["gtm.uniqueEventId"] = c, ef("gtm.uniqueEventId", c));
        return aj(a)
    }

    function tm() {
        var a = mm[0];
        if (null == a || "object" !== typeof a) return !1;
        if (a.event) return !0;
        if (Qa(a)) {
            var b = a[0];
            if ("config" === b || "event" === b || "js" === b) return !0
        }
        return !1
    }

    function um() {
        for (var a = !1; !pm && 0 < mm.length;) {
            var b = !1;
            b = !1;
            b = !0;
            if (b && !om && tm()) {
                var c = {};
                mm.unshift((c.event =
                    "gtm.init", c));
                om = !0
            }
            var d = !1;
            d = !1;
            if (d && !nm && tm()) {
                var e = {};
                mm.unshift((e.event = "gtm.init_consent", e));
                nm = !0
            }
            pm = !0;
            delete Ze.eventModel;
            af();
            var f = mm.shift();
            if (null != f) {
                var h = km(f);
                if (h) {
                    var k = f;
                    f = km(k) ? k.getUntrustedUpdateValue() : void 0;
                    for (var l = ["gtm.allowlist", "gtm.blocklist", "gtm.whitelist", "gtm.blacklist", "tagTypeBlacklist"], p = 0; p < l.length; p++) {
                        var q = l[p], n = bf(q, 1);
                        if (Ha(n) || ob(n)) n = m(n);
                        $e[q] = n
                    }
                }
                try {
                    if (Da(f)) try {
                        f.call(cf)
                    } catch (A) {
                    } else if (Ha(f)) {
                        var u = f;
                        if (g(u[0])) {
                            var t = u[0].split("."), r = t.pop(), v = u.slice(1), w = bf(t.join("."), 2);
                            if (void 0 !== w && null !== w) try {
                                w[r].apply(w, v)
                            } catch (A) {
                            }
                        }
                    } else {
                        if (Qa(f)) {
                            a:{
                                var y = f;
                                if (y.length && g(y[0])) {
                                    var x = Pl[y[0]];
                                    if (x && (!h || !Ql[y[0]])) {
                                        f =
                                            x(y);
                                        break a
                                    }
                                }
                                f = void 0
                            }
                            if (!f) {
                                pm = !1;
                                continue
                            }
                        }
                        a = sm(f) || a
                    }
                } finally {
                    h && af(!0)
                }
            }
            pm = !1
        }
        return !a
    }

    function vm() {
        var a = um();
        try {
            Rl(G["dataLayer"], Je.D)
        } catch (b) {
        }
        return a
    }

    var xm = function () {
        var a = cd("dataLayer", []), b = cd("google_tag_manager", {});
        b = b["dataLayer"] = b["dataLayer"] || {};
        Rh(function () {
            b.gtmDom || (b.gtmDom = !0, a.push({event: "gtm.dom"}))
        });
        Gl(function () {
            b.gtmLoad || (b.gtmLoad = !0, a.push({event: "gtm.load"}))
        });
        b.subscribers = (b.subscribers || 0) + 1;
        var c = a.push;
        a.push = function () {
            var e;
            if (0 < L.SANDBOXED_JS_SEMAPHORE) {
                e = [];
                for (var f = 0; f < arguments.length; f++) e[f] = new lm(arguments[f])
            } else e = [].slice.call(arguments, 0);
            var h = c.apply(a, e);
            mm.push.apply(mm, e);
            if (300 <
                this.length) for (F(4); 300 < this.length;) this.shift();
            var k = "boolean" !== typeof h || h;
            return um() && k
        };
        var d = a.slice(0);
        mm.push.apply(mm, d);
        wm() && J(vm)
    }, wm = function () {
        var a = !0;
        return a
    };
    var ym = {};
    ym.ic = new String("undefined");
    var zm = function (a) {
        this.m = function (b) {
            for (var c = [], d = 0; d < a.length; d++) c.push(a[d] === ym.ic ? b : a[d]);
            return c.join("")
        }
    };
    zm.prototype.toString = function () {
        return this.m("undefined")
    };
    zm.prototype.valueOf = zm.prototype.toString;
    ym.Mg = zm;
    ym.md = {};
    ym.mh = function (a) {
        return new zm(a)
    };
    var Am = {};
    ym.ei = function (a, b) {
        var c = We();
        Am[c] = [a, b];
        return c
    };
    ym.lf = function (a) {
        var b = a ? 0 : 1;
        return function (c) {
            var d = Am[c];
            if (d && "function" === typeof d[b]) d[b]();
            Am[c] = void 0
        }
    };
    ym.Ih = function (a) {
        for (var b = !1, c = !1, d = 2; d < a.length; d++) b =
            b || 8 === a[d], c = c || 16 === a[d];
        return b && c
    };
    ym.Yh = function (a) {
        if (a === ym.ic) return a;
        var b = We();
        ym.md[b] = a;
        return 'google_tag_manager["' + Je.D + '"].macro(' + b + ")"
    };
    ym.Sh = function (a, b, c) {
        a instanceof ym.Mg && (a = a.m(ym.ei(b, c)), b = Ba);
        return {Ad: a, onSuccess: b}
    };
    var Bm = ["input", "select", "textarea"], Cm = ["button", "hidden", "image", "reset", "submit"], Dm = function (a) {
        var b = a.tagName.toLowerCase();
        return !Ja(Bm, function (c) {
            return c === b
        }) || "input" === b && Ja(Cm, function (c) {
            return c === a.type.toLowerCase()
        }) ? !1 : !0
    }, Em = function (a) {
        return a.form ? a.form.tagName ? a.form : H.getElementById(a.form) : pd(a, ["form"], 100)
    }, Fm = function (a, b, c) {
        if (!a.elements) return 0;
        for (var d = b.getAttribute(c), e = 0, f = 1; e < a.elements.length; e++) {
            var h = a.elements[e];
            if (Dm(h)) {
                if (h.getAttribute(c) === d) return f;
                f++
            }
        }
        return 0
    };
    var Qm = G.clearTimeout, Rm = G.setTimeout, P = function (a, b, c) {
        if (eh()) {
            b && J(b)
        } else return gd(a, b, c)
    }, Sm = function () {
        return new Date
    }, Tm = function () {
        return G.location.href
    }, Um = function (a) {
        return ke(me(a), "fragment")
    }, Vm = function (a) {
        return le(me(a))
    }, Wm = function (a, b) {
        return bf(a, b || 2)
    }, Xm = function (a, b, c) {
        var d;
        b ? (a.eventCallback = b, c && (a.eventTimeout = c), d = qm(a)) : d = qm(a);
        return d
    }, Ym = function (a, b) {
        G[a] = b
    }, U = function (a, b, c) {
        b &&
        (void 0 === G[a] || c && !G[a]) && (G[a] = b);
        return G[a]
    }, Zm = function (a, b, c) {
        return sf(a, b, void 0 === c ? !0 : !!c)
    }, $m = function (a, b, c) {
        return 0 === Bf(a, b, c)
    }, an = function (a, b) {
        if (eh()) {
            b && J(b)
        } else id(a, b)
    }, bn = function (a) {
        return !!yl(a, "init", !1)
    }, cn = function (a) {
        wl(a, "init", !0)
    }, dn = function (a, b) {
        var c = (void 0 === b ? 0 : b) ? "www.googletagmanager.com/gtag/js" : Pe;
        c += "?id=" + encodeURIComponent(a) + "&l=dataLayer";
        P(gh("https://", "http://", c))
    }, en = function (a,
                      b) {
        var c = a[b];
        c = rd(a, b);
        return c
    }, fn = function (a, b, c) {
        Ii && (pb(a) || Mi(c, b, a))
    };
    var gn = ym.Sh;

    function En(a, b) {
        a = String(a);
        b = String(b);
        var c = a.length - b.length;
        return 0 <= c && a.indexOf(b, c) == c
    }

    var Fn = new La;

    function Gn(a, b) {
        function c(h) {
            var k = me(h), l = ke(k, "protocol"), p = ke(k, "host", !0), q = ke(k, "port"),
                n = ke(k, "path").toLowerCase().replace(/\/$/, "");
            if (void 0 === l || "http" == l && "80" == q || "https" == l && "443" == q) l = "web", q = "default";
            return [l, p, q, n]
        }

        for (var d = c(String(a)), e = c(String(b)), f = 0; f < d.length; f++) if (d[f] !== e[f]) return !1;
        return !0
    }

    function Hn(a) {
        return In(a) ? 1 : 0
    }

    function In(a) {
        var b = a.arg0, c = a.arg1;
        if (a.any_of && Ha(c)) {
            for (var d = 0; d < c.length; d++) {
                var e = m(a, {});
                m({arg1: c[d], any_of: void 0}, e);
                if (Hn(e)) return !0
            }
            return !1
        }
        switch (a["function"]) {
            case "_cn":
                return 0 <= String(b).indexOf(String(c));
            case "_css":
                var f;
                a:{
                    if (b) {
                        var h = ["matches", "webkitMatchesSelector", "mozMatchesSelector", "msMatchesSelector", "oMatchesSelector"];
                        try {
                            for (var k = 0; k < h.length; k++) if (b[h[k]]) {
                                f = b[h[k]](c);
                                break a
                            }
                        } catch (t) {
                        }
                    }
                    f = !1
                }
                return f;
            case "_ew":
                return En(b, c);
            case "_eq":
                return String(b) ==
                    String(c);
            case "_ge":
                return Number(b) >= Number(c);
            case "_gt":
                return Number(b) > Number(c);
            case "_lc":
                var l;
                l = String(b).split(",");
                return 0 <= Ia(l, String(c));
            case "_le":
                return Number(b) <= Number(c);
            case "_lt":
                return Number(b) < Number(c);
            case "_re":
                var p;
                var q = a.ignore_case ? "i" : void 0;
                try {
                    var n = String(c) + q, u = Fn.get(n);
                    u || (u = new RegExp(c, q), Fn.set(n, u));
                    p = u.test(b)
                } catch (t) {
                    p = !1
                }
                return p;
            case "_sw":
                return 0 == String(b).indexOf(String(c));
            case "_um":
                return Gn(b, c)
        }
        return !1
    };var Jn = encodeURI, X = encodeURIComponent, Kn = jd;
    var Ln = function (a, b) {
        if (!a) return !1;
        var c = ke(me(a), "host");
        if (!c) return !1;
        for (var d = 0; b && d < b.length; d++) {
            var e = b[d] && b[d].toLowerCase();
            if (e) {
                var f = c.length - e.length;
                0 < f && "." != e.charAt(0) && (f--, e = "." + e);
                if (0 <= f && c.indexOf(e, f) == f) return !0
            }
        }
        return !1
    };
    var Mn = function (a, b, c) {
        for (var d = {}, e = !1, f = 0; a && f < a.length; f++) a[f] && a[f].hasOwnProperty(b) && a[f].hasOwnProperty(c) && (d[a[f][b]] = a[f][c], e = !0);
        return e ? d : null
    };

    function sp() {
        return G.gaGlobal = G.gaGlobal || {}
    }

    var tp = function () {
        var a = sp();
        a.hid = a.hid || Ka();
        return a.hid
    }, up = function (a, b) {
        var c = sp();
        if (void 0 == c.vid || b && !c.from_cookie) c.vid = a, c.from_cookie = b
    };
    var eq = window, fq = document, gq = function (a) {
        var b = eq._gaUserPrefs;
        if (b && b.ioo && b.ioo() || a && !0 === eq["ga-disable-" + a]) return !0;
        try {
            var c = eq.external;
            if (c && c._gaUserPrefs && "oo" == c._gaUserPrefs) return !0
        } catch (f) {
        }
        for (var d = qf("AMP_TOKEN", String(fq.cookie), !0), e = 0; e < d.length; e++) if ("$OPT_OUT" == d[e]) return !0;
        return fq.getElementById("__gaOptOutExtension") ? !0 : !1
    };
    var hq = {};

    function jq(a) {
        delete a.eventModel[C.Ab];
        lq(a.eventModel)
    }

    var lq = function (a) {
        Oa(a, function (c) {
            "_" === c.charAt(0) && delete a[c]
        });
        var b = a[C.na] || {};
        Oa(b, function (c) {
            "_" === c.charAt(0) && delete b[c]
        })
    };
    var oq = function (a, b, c) {
        ll(b, c, a)
    }, pq = function (a, b, c) {
        ll(b, c, a, !0)
    }, uq = function (a, b) {
    };

    function qq(a, b) {
    }

    var Z = {g: {}};

    Z.g.gtagha = ["google"], function () {
        (function (a) {
            Z.__gtagha = a;
            Z.__gtagha.h = "gtagha";
            Z.__gtagha.i = !0;
            Z.__gtagha.priorityOverride = 0
        })(function (a) {
            J(a.vtp_gtmOnSuccess)
        })
    }();
    Z.g.e = ["google"], function () {
        (function (a) {
            Z.__e = a;
            Z.__e.h = "e";
            Z.__e.i = !0;
            Z.__e.priorityOverride = 0
        })(function (a) {
            return String(gf(a.vtp_gtmEventId, "event"))
        })
    }();

    Z.g.v = ["google"], function () {
        (function (a) {
            Z.__v = a;
            Z.__v.h = "v";
            Z.__v.i = !0;
            Z.__v.priorityOverride = 0
        })(function (a) {
            var b = a.vtp_name;
            if (!b || !b.replace) return !1;
            var c = Wm(b.replace(/\\\./g, "."), a.vtp_dataLayerVersion || 1), d = void 0 !== c ? c : a.vtp_defaultValue;
            fn(d, "v", a.vtp_gtmEventId);
            return d
        })
    }();

    Z.g.rep = ["google"], function () {
        (function (a) {
            Z.__rep = a;
            Z.__rep.h = "rep";
            Z.__rep.i = !0;
            Z.__rep.priorityOverride = 0
        })(function (a) {
            var b;
            switch (bh(a.vtp_containerId).prefix) {
                case "AW":
                    b = lk;
                    break;
                case "DC":
                    b = xk;
                    break;
                case "GF":
                    b = Ck;
                    break;
                case "HA":
                    b = Hk;
                    break;
                case "UA":
                    b = al;
                    break;
                default:
                    J(a.vtp_gtmOnFailure);
                    return
            }
            J(a.vtp_gtmOnSuccess);
            kl(a.vtp_containerId, b, a.vtp_remoteConfig || {})
        })
    }();


    Z.g.cid = ["google"], function () {
        (function (a) {
            Z.__cid = a;
            Z.__cid.h = "cid";
            Z.__cid.i = !0;
            Z.__cid.priorityOverride = 0
        })(function () {
            return Je.D
        })
    }();


    Z.g.gtagaw = ["google"], function () {
        (function (a) {
            Z.__gtagaw = a;
            Z.__gtagaw.h = "gtagaw";
            Z.__gtagaw.i = !0;
            Z.__gtagaw.priorityOverride = 0
        })(function (a) {
            var b = "AW-" + String(a.vtp_conversionId);
            ll(String(a.vtp_eventName), a.vtp_eventData || {}, a.vtp_conversionLabel ? b + "/" + String(a.vtp_conversionLabel) : b);
            J(a.vtp_gtmOnSuccess)
        })
    }();

    Z.g.get = ["google"], function () {
        (function (a) {
            Z.__get = a;
            Z.__get.h = "get";
            Z.__get.i = !0;
            Z.__get.priorityOverride = 0
        })(function (a) {
            var b = a.vtp_settings;
            (a.vtp_deferrable ? pq : oq)(String(b.streamId), String(a.vtp_eventName), b.eventParameters || {});
            a.vtp_gtmOnSuccess()
        })
    }();


    Z.g.gtagfl = [], function () {
        (function (a) {
            Z.__gtagfl = a;
            Z.__gtagfl.h = "gtagfl";
            Z.__gtagfl.i = !0;
            Z.__gtagfl.priorityOverride = 0
        })(function (a) {
            J(a.vtp_gtmOnSuccess)
        })
    }();

    Z.g.gtaggf = ["google"], function () {
        (function (a) {
            Z.__gtaggf = a;
            Z.__gtaggf.h = "gtaggf";
            Z.__gtaggf.i = !0;
            Z.__gtaggf.priorityOverride = 0
        })(function (a) {
            J(a.vtp_gtmOnSuccess)
        })
    }();


    Z.g.gtagua = ["google"], function () {
        (function (a) {
            Z.__gtagua = a;
            Z.__gtagua.h = "gtagua";
            Z.__gtagua.i = !0;
            Z.__gtagua.priorityOverride = 0
        })(function (a) {
            J(a.vtp_gtmOnSuccess)
        })
    }();


    var vq = {};
    vq.macro = function (a) {
        if (ym.md.hasOwnProperty(a)) return ym.md[a]
    }, vq.onHtmlSuccess = ym.lf(!0), vq.onHtmlFailure = ym.lf(!1);
    vq.dataLayer = cf;
    vq.callback = function (a) {
        Ue.hasOwnProperty(a) && Da(Ue[a]) && Ue[a]();
        delete Ue[a]
    };
    vq.bootstrap = 0;
    vq._spx = !1;

    function wq() {
        L[Je.D] = vq;
        db(Ve, Z.g);
        Yb = Yb || ym;
        Zb = jc
    }

    function xq() {
        sd.gtag_cs_api = !0;
        L = G.google_tag_manager = G.google_tag_manager || {};
        Uj();
        if (L[Je.D]) {
            var a = L.zones;
            a && a.unregisterChild(Je.D);
        } else {
            for (var b = data.resource || {}, c = b.macros || [], d = 0; d < c.length; d++) Rb.push(c[d]);
            for (var e = b.tags || [], f = 0; f < e.length; f++) Ub.push(e[f]);
            for (var h = b.predicates || [], k = 0; k < h.length; k++) Tb.push(h[k]);
            for (var l = b.rules || [], p = 0; p < l.length; p++) {
                for (var q = l[p], n = {}, u = 0; u < q.length; u++) n[q[u][0]] = Array.prototype.slice.call(q[u], 1);
                Sb.push(n)
            }
            Wb = Z;
            Xb = Hn;
            wq();
            xm();
            Ih = !1;
            Nh = 0;
            if ("interactive" == H.readyState && !H.createEventObject || "complete" == H.readyState) Ph(); else {
                kd(H, "DOMContentLoaded", Ph);
                kd(H, "readystatechange", Ph);
                if (H.createEventObject && H.documentElement.doScroll) {
                    var t = !0;
                    try {
                        t = !G.frameElement
                    } catch (x) {
                    }
                    t &&
                    Qh()
                }
                kd(G, "load", Ph)
            }
            Dl = !1;
            "complete" === H.readyState ? Fl() : kd(G, "load", Fl);
            a:{
                if (!Ii) break a;
                G.setInterval(Ji, 864E5);
            }
            Se = (new Date).getTime();
            vq.bootstrap = Se;
        }
    }

    (function (a) {
        if (!G["__TAGGY_INSTALLED"]) {
            var b = !1;
            if (H.referrer) {
                var c = me(H.referrer);
                b = "cct.google" === je(c, "host")
            }
            if (!b) {
                var d = sf("googTaggyReferrer");
                b = d.length && d[0].length
            }
            b && (G["__TAGGY_INSTALLED"] = !0, gd("https://cct.google/taggy/agent.js"))
        }
        var f = function () {
            var p = G["google.tagmanager.debugui2.queue"];
            p || (p = [], G["google.tagmanager.debugui2.queue"] = p, gd("https://www.googletagmanager.com/debug/bootstrap"));
            return p
        }, h = "x" === ke(G.location, "query", !1, void 0, "gtm_debug");
        if (!h && H.referrer) {
            var k = me(H.referrer);
            h = "tagassistant.google.com" === je(k, "host")
        }
        if (!h) {
            var l = sf("__TAG_ASSISTANT");
            h = l.length && l[0].length
        }
        G.__TAG_ASSISTANT_API && (h = !0);
        h && bd ? f().push({
            messageType: "CONTAINER_STARTING",
            data: {
                scriptSource: bd, resume: function () {
                    a()
                }
            }
        }) : a()
    })(xq);

})()
