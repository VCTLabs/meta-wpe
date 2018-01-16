SUMMARY = "WPE Framework common plugins"
HOMEPAGE = "https://github.com/WebPlatformForEmbedded"
SECTION = "wpe"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

DEPENDS = "wpeframework dvb-apps inotify-tools alsa-lib"

PV = "3.0+gitr${SRCPV}"

SRC_URI = "git://git@github.com/WebPlatformForEmbedded/WPEFrameworkPlugins.git;protocol=ssh;branch=tvcontrol \
          file://0001-Compositor-Disable-building-of-the-Wayland-test-clie.patch \
          file://index.html \
          file://wpeframework-plugins-tvcontrol-init \
          file://wpeframework-plugins-tvcontrol.service "

SRCREV = "4a36356d256504ce7c1423b27ede9fd5007fa4aa"

S = "${WORKDIR}/git"

WEBKITBROWSER_AUTOSTART ?= "true"
WEBKITBROWSER_MEDIADISKCACHE ?= "false"
WEBKITBROWSER_MEMORYPRESSURE ?= "databaseprocess:50m,networkprocess:100m,webprocess:300m,rpcprocess:50m"
WEBKITBROWSER_MEMORYPROFILE ?= "128m"
WEBKITBROWSER_STARTURL ?= "http://cdn.metrological.com/static/rpi/index.html"
WEBKITBROWSER_USERAGENT ?= "Mozilla/5.0 (Macintosh, Intel Mac OS X 10_11_4) AppleWebKit/602.1.28+ (KHTML, like Gecko) Version/9.1 Safari/601.5.17"
WEBKITBROWSER_DISKCACHE ?= "0"
WEBKITBROWSER_XHRCACHE ?= "false"

WPEFRAMEWORK_LOCATIONSYNC_URI ?= "location.webplatformforembedded.org"
WPEFRAMEWORK_REMOTECONTROL_NEXUS_IRMODE ?= "16"

WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT ?= "8080"
WPEFRAMEWORK_PLUGIN_WEBSERVER_BIND ?= "0.0.0.0"
WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH ?= "/var/www/"

WPE_SNAPSHOT ?= ""
WPE_SNAPSHOT_rpi = "snapshot"

WPE_TVCONTROL ?= ""
WPE_TVCONTROL_rpi = "tvcontrol"

WPE_TVCONTROL_PACKAGES = ""
WPE_TVCONTROL_PACKAGES_rpi = "dvb-apps inotify-tools"

RDEPS_TVCONTROL ?= ""
RDEPS_TVCONTROL_rpi = "gstreamer1.0-plugins-bad-dvb \
    gstreamer1.0-plugins-base-alsa \
    gstreamer1.0-libav"

WPE_TVCONTROL_FLAGS ?= ""
WPE_TVCONTROL_FLAGS_rpi = "-DWPEFRAMEWORK_PLUGIN_TVCONTROL_DVB="false" \
    -DWPEFRAMEWORK_PLUGINS_TVCONTROL_FREQUENCY_LIST="575" \
    "
WPE_TVCONTROL_FLAGS_bcm = "-DWPEFRAMEWORK_PLUGIN_TVCONTROL_DVB="true" \
    -DWPEFRAMEWORK_PLUGINS_TVCONTROL_FREQUENCY_LIST="575" \
    -DWPEFRAMEWORK_PLUGINS_TVCONTROL_COUNTRY_REGION_ID="0" \
    -DWPEFRAMEWORK_PLUGINS_TVCONTROL_COUNTRY_CODE="GBR" \
    -DWPEFRAMEWORK_PLUGINS_TVCONTROL_TUNE_PARAM="SYMBOL_RATE=6900000" \
    "

## Compositor settings, if Wayland is in the distro set the implementation to Wayland with Westeros dependency
WPE_COMPOSITOR ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'compositor', '', d)}"
WPE_COMPOSITOR_IMPL = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'Wayland', 'None', d)}"
WPE_COMPOSITOR_DEP = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'westeros', '', d)}"

# override to nexus for bcm nexus enabled builds
WPE_COMPOSITOR_nexus = "compositor"
WPE_COMPOSITOR_IMPL_nexus = "Nexus"
WPE_COMPOSITOR_DEP_nexus = "broadcom-refsw"

inherit cmake pkgconfig systemd update-rc.d

PACKAGECONFIG ?= "commander ${WPE_COMPOSITOR} deviceinfo remote remote-uinput ${WPE_SNAPSHOT} tracing virtualinput webkitbrowser webshell webserver youtube ${WPE_TVCONTROL}"

PACKAGECONFIG[commander]      = "-DWPEFRAMEWORK_PLUGIN_COMMANDER=ON,-DWPEFRAMEWORK_PLUGIN_COMMANDER=OFF,"
PACKAGECONFIG[compositor]     = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=ON -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_IMPLEMENTATION=${WPE_COMPOSITOR_IMPL} -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=OFF,${WPE_COMPOSITOR_DEP}"
PACKAGECONFIG[debug]          = "-DCMAKE_BUILD_TYPE=Debug,-DCMAKE_BUILD_TYPE=Release,"
PACKAGECONFIG[deviceinfo]     = "-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=ON,-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=OFF,"
PACKAGECONFIG[locationsync]   = "-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=ON \
   -DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC_URI=${WPEFRAMEWORK_LOCATIONSYNC_URI} \
   ,-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=OFF,"
PACKAGECONFIG[monitor] 			  = "-DWPEFRAMEWORK_PLUGIN_MONITOR=ON,-DWPEFRAMEWORK_PLUGIN_MONITOR=OFF,"
PACKAGECONFIG[power]          = "-DWPEFRAMEWORK_PLUGIN_POWER=ON,-DWPEFRAMEWORK_PLUGIN_POWER=OFF,"
PACKAGECONFIG[remote]         = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=ON,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=OFF,"
PACKAGECONFIG[remote-nexus]   = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=ON \
   -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRMODE=${WPEFRAMEWORK_REMOTECONTROL_IRMODE} \
   ,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=OFF,"
PACKAGECONFIG[remote-uinput]  = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVUINPUT=ON,-DDWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVUINPUT=OFF,"
PACKAGECONFIG[snapshot] 		  = "-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=ON,-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=OFF,userland libpng"
PACKAGECONFIG[timesync] 		  = "-DWPEFRAMEWORK_PLUGIN_TIMESYNC=ON,-DWPEFRAMEWORK_PLUGIN_TIMESYNC=OFF,"
PACKAGECONFIG[tracing] 			  = "-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=ON,-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=OFF,"
PACKAGECONFIG[virtualinput]   = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=OFF,"
PACKAGECONFIG[webkitbrowser]  = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=ON \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_AUTOSTART="${WEBKITBROWSER_AUTOSTART}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEDIADISKCACHE="${WEBKITBROWSER_MEDIADISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPRESSURE="${WEBKITBROWSER_MEMORYPRESSURE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPROFILE="${WEBKITBROWSER_MEMORYPROFILE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_STARTURL="${WEBKITBROWSER_STARTURL}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_USERAGENT="${WEBKITBROWSER_USERAGENT}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_DISKCACHE="${WEBKITBROWSER_DISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_XHRCACHE="${WEBKITBROWSER_XHRCACHE}" \
   ,-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=OFF,wpewebkit"
PACKAGECONFIG[webproxy]       = "-DWPEFRAMEWORK_PLUGIN_WEBPROXY=ON,-DWPEFRAMEWORK_PLUGIN_WEBPROXY=OFF,"
PACKAGECONFIG[webserver]      = "-DWPEFRAMEWORK_PLUGIN_WEBSERVER=ON \
    -DWPEFRAMEWORK_PLUGIN_WEBSERVER_PORT="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT}" \
    -DWPEFRAMEWORK_PLUGIN_WEBSERVER_BINDING="${WPEFRAMEWORK_PLUGIN_WEBSERVER_BIND}" \
    -DWPEFRAMEWORK_PLUGIN_WEBSERVER_PATH="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}" \
    ,-DWPEFRAMEWORK_PLUGIN_WEBSERVER=OFF,"
PACKAGECONFIG[webshell]       = "-DWPEFRAMEWORK_PLUGIN_WEBSHELL=ON,-DWPEFRAMEWORK_PLUGIN_WEBSHELL=OFF,"
PACKAGECONFIG[wifi]           = "-DWPEFRAMEWORK_PLUGIN_WIFISETUP=ON,-DWPEFRAMEWORK_PLUGIN_WIFISETUP=OFF,"
PACKAGECONFIG[youtube]        = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=ON, -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=OFF,,wpeframework-dialserver"
PACKAGECONFIG[tvcontrol]      = "-DWPEFRAMEWORK_PLUGIN_TVCONTROL=ON \
    ${WPE_TVCONTROL_FLAGS} ,-DWPEFRAMEWORK_PLUGIN_TVCONTROL=OFF,sqlite,${WPE_TVCONTROL_PACKAGES},${RDEPS_TVCONTROL}"

EXTRA_OECMAKE += " \
    -DBUILD_REFERENCE=${SRCREV} \
    -DBUILD_SHARED_LIBS=ON \
"
do_install_append() {
    if ${@bb.utils.contains("PACKAGECONFIG", "webserver", "true", "false", d)}
    then
      if ${@bb.utils.contains("PACKAGECONFIG", "webkitbrowser", "true", "false", d)}
      then
          install -d ${D}/var/www
          install -m 0755 ${WORKDIR}/index.html ${D}/var/www/
      fi
      if ${@bb.utils.contains("PACKAGECONFIG", "tvcontrol", "true", "flase", d)}
      then
          if [ "${SOC_FAMILY}" = "rpi" ]; then
              if ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "true", "false", d)}; then
                  install -d ${D}${systemd_unitdir}/system
                  install -m 0755 ${WORKDIR}/wpeframework-plugins-tvcontrol.service ${D}${systemd_unitdir}/system/wpeframework-plugins-tvcontrol.service
              else
                  install -d ${D}${sysconfdir}/init.d
                  install -m 0755 ${WORKDIR}/wpeframework-plugins-tvcontrol-init ${D}${sysconfdir}/init.d/wpeframework-plugins-tvcontrol
              fi
          fi
      fi
      install -d ${D}${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}
    fi
}

SYSTEMD_SERVICE_${PN} = "wpeframework-plugins-tvcontrol.service"
PACKAGES =+ "${PN}-initscript"

FILES_${PN}-initscript = "${sysconfdir}/init.d/wpeframework-plugins-tvcontrol"

# ----------------------------------------------------------------------------

INITSCRIPT_PACKAGES = "${PN}-initscript"
INITSCRIPT_NAME_${PN}-initscript = "wpeframework-plugins-tvcontrol"
INITSCRIPT_PARAMS_${PN}-initscript = "defaults 90 90"

RRECOMMENDS_${PN} = "${PN}-initscript"
# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so ${libdir}/libwaylandeglclient.so ${datadir}/WPEFramework/* /var/www/index.html"

INSANE_SKIP_${PN} += "libdir staticdev"

TOOLCHAIN = "gcc"
