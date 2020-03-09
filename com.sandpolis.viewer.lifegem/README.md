## Sandpolis Graphical Viewer (`com.sandpolis.viewer.lifegem`)

The GUI viewer is a rich desktop application that allows you to remotely control Sandpolis clients.

##### Linux
To allow the container to access the host's X server, run:
```sh
xhost local:root
```

To run the container:
```sh
docker run -it -v /tmp/.X11-unix/:/tmp/.X11-unix/ -e DISPLAY --net host sandpolis/viewer/lifegem:debug
```