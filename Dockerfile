FROM eclipse-temurin:17

USER root
RUN apt-get update \
    && apt-get install -y graphviz \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /opt/structurizr-site-generatr \
    && chown 65532:65532 /opt/structurizr-site-generatr

USER 65532
ENTRYPOINT ["/opt/structurizr-site-generatr/bin/structurizr-site-generatr"]

WORKDIR /opt/structurizr-site-generatr
COPY build/install/structurizr-site-generatr ./
