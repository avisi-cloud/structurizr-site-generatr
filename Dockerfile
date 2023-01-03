FROM eclipse-temurin:19-jre-jammy

USER root
RUN apt update && apt install graphviz --yes
RUN mkdir -p /var/model \
    && chown 65532:65532 /var/model
RUN adduser -h /home/generatr -u 65532 -D generatr

ENTRYPOINT ["/opt/structurizr-site-generatr/bin/structurizr-site-generatr"]

WORKDIR /opt/structurizr-site-generatr
COPY build/install/structurizr-site-generatr ./
RUN chmod +x /opt/structurizr-site-generatr/bin/structurizr-site-generatr

USER generatr
VOLUME ["/var/model"]
WORKDIR /var/model
