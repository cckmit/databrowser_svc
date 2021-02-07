
FROM openjdk:8-jre-alpine
MAINTAINER allen.jin <allen.jin@chinascope.com>

ARG VERSION
ENV VERSION $VERSION

RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache ttf-dejavu procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD target/browser-dist-${VERSION}*.tar.gz /app/services/
RUN  ln -s /app/services/browser-dist-${VERSION} /app/services/browser-dist \
  && chmod +x /app/services/browser-dist/bin/*.sh

EXPOSE 8080

ENTRYPOINT ["/app/services/browser-dist/bin/start_inside.sh"]
CMD []
