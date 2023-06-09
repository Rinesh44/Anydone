// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: events.proto

package com.treeleaf.anydone.entities;

public final class EventsProto {
  private EventsProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  /**
   * Protobuf enum {@code oyster.entities.pb.EventType}
   */
  public enum EventType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>UNKNOWN_EVENT_TYPE = 0;</code>
     */
    UNKNOWN_EVENT_TYPE(0),
    /**
     * <code>NOTIFY = 1;</code>
     */
    NOTIFY(1),
    /**
     * <code>QUEUE = 2;</code>
     */
    QUEUE(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>UNKNOWN_EVENT_TYPE = 0;</code>
     */
    public static final int UNKNOWN_EVENT_TYPE_VALUE = 0;
    /**
     * <code>NOTIFY = 1;</code>
     */
    public static final int NOTIFY_VALUE = 1;
    /**
     * <code>QUEUE = 2;</code>
     */
    public static final int QUEUE_VALUE = 2;


    public final int getNumber() {
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static EventType valueOf(int value) {
      return forNumber(value);
    }

    public static EventType forNumber(int value) {
      switch (value) {
        case 0: return UNKNOWN_EVENT_TYPE;
        case 1: return NOTIFY;
        case 2: return QUEUE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<EventType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        EventType> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<EventType>() {
            public EventType findValueByNumber(int number) {
              return EventType.forNumber(number);
            }
          };

    private final int value;

    private EventType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:oyster.entities.pb.EventType)
  }

  public interface EventOrBuilder extends
      // @@protoc_insertion_point(interface_extends:oyster.entities.pb.Event)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    int getTypeValue();
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    com.treeleaf.anydone.entities.EventsProto.EventType getType();

    /**
     * <code>optional bool immediate = 2;</code>
     */
    boolean getImmediate();

    /**
     * <pre>
     *All general payload.
     * </pre>
     *
     * <code>optional bytes payload = 3;</code>
     */
    com.google.protobuf.ByteString getPayload();

    /**
     * <pre>
     *Used on reliable queue
     * </pre>
     *
     * <code>optional int64 timestamp = 4;</code>
     */
    long getTimestamp();

    /**
     * <code>optional string eventId = 5;</code>
     */
    java.lang.String getEventId();
    /**
     * <code>optional string eventId = 5;</code>
     */
    com.google.protobuf.ByteString
        getEventIdBytes();

    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    boolean hasDebug();
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    com.treeleaf.anydone.entities.TreeleafProto.Debug getDebug();

    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    java.lang.String getEventGeneratedHost();
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    com.google.protobuf.ByteString
        getEventGeneratedHostBytes();

    /**
     * <code>optional string emitter = 8;</code>
     */
    java.lang.String getEmitter();
    /**
     * <code>optional string emitter = 8;</code>
     */
    com.google.protobuf.ByteString
        getEmitterBytes();
  }
  /**
   * <pre>
   *Generic Event object
   * </pre>
   *
   * Protobuf type {@code oyster.entities.pb.Event}
   */
  public  static final class Event extends
      com.google.protobuf.GeneratedMessageLite<
          Event, Event.Builder> implements
      // @@protoc_insertion_point(message_implements:oyster.entities.pb.Event)
      EventOrBuilder {
    private Event() {
      payload_ = com.google.protobuf.ByteString.EMPTY;
      eventId_ = "";
      eventGeneratedHost_ = "";
      emitter_ = "";
    }
    public static final int TYPE_FIELD_NUMBER = 1;
    private int type_;
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    public int getTypeValue() {
      return type_;
    }
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    public com.treeleaf.anydone.entities.EventsProto.EventType getType() {
      com.treeleaf.anydone.entities.EventsProto.EventType result = com.treeleaf.anydone.entities.EventsProto.EventType.forNumber(type_);
      return result == null ? com.treeleaf.anydone.entities.EventsProto.EventType.UNRECOGNIZED : result;
    }
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    private void setTypeValue(int value) {
        type_ = value;
    }
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    private void setType(com.treeleaf.anydone.entities.EventsProto.EventType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      type_ = value.getNumber();
    }
    /**
     * <code>optional .oyster.entities.pb.EventType type = 1;</code>
     */
    private void clearType() {
      
      type_ = 0;
    }

    public static final int IMMEDIATE_FIELD_NUMBER = 2;
    private boolean immediate_;
    /**
     * <code>optional bool immediate = 2;</code>
     */
    public boolean getImmediate() {
      return immediate_;
    }
    /**
     * <code>optional bool immediate = 2;</code>
     */
    private void setImmediate(boolean value) {
      
      immediate_ = value;
    }
    /**
     * <code>optional bool immediate = 2;</code>
     */
    private void clearImmediate() {
      
      immediate_ = false;
    }

    public static final int PAYLOAD_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString payload_;
    /**
     * <pre>
     *All general payload.
     * </pre>
     *
     * <code>optional bytes payload = 3;</code>
     */
    public com.google.protobuf.ByteString getPayload() {
      return payload_;
    }
    /**
     * <pre>
     *All general payload.
     * </pre>
     *
     * <code>optional bytes payload = 3;</code>
     */
    private void setPayload(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      payload_ = value;
    }
    /**
     * <pre>
     *All general payload.
     * </pre>
     *
     * <code>optional bytes payload = 3;</code>
     */
    private void clearPayload() {
      
      payload_ = getDefaultInstance().getPayload();
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 4;
    private long timestamp_;
    /**
     * <pre>
     *Used on reliable queue
     * </pre>
     *
     * <code>optional int64 timestamp = 4;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <pre>
     *Used on reliable queue
     * </pre>
     *
     * <code>optional int64 timestamp = 4;</code>
     */
    private void setTimestamp(long value) {
      
      timestamp_ = value;
    }
    /**
     * <pre>
     *Used on reliable queue
     * </pre>
     *
     * <code>optional int64 timestamp = 4;</code>
     */
    private void clearTimestamp() {
      
      timestamp_ = 0L;
    }

    public static final int EVENTID_FIELD_NUMBER = 5;
    private java.lang.String eventId_;
    /**
     * <code>optional string eventId = 5;</code>
     */
    public java.lang.String getEventId() {
      return eventId_;
    }
    /**
     * <code>optional string eventId = 5;</code>
     */
    public com.google.protobuf.ByteString
        getEventIdBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(eventId_);
    }
    /**
     * <code>optional string eventId = 5;</code>
     */
    private void setEventId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      eventId_ = value;
    }
    /**
     * <code>optional string eventId = 5;</code>
     */
    private void clearEventId() {
      
      eventId_ = getDefaultInstance().getEventId();
    }
    /**
     * <code>optional string eventId = 5;</code>
     */
    private void setEventIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      eventId_ = value.toStringUtf8();
    }

    public static final int DEBUG_FIELD_NUMBER = 6;
    private com.treeleaf.anydone.entities.TreeleafProto.Debug debug_;
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    public boolean hasDebug() {
      return debug_ != null;
    }
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    public com.treeleaf.anydone.entities.TreeleafProto.Debug getDebug() {
      return debug_ == null ? com.treeleaf.anydone.entities.TreeleafProto.Debug.getDefaultInstance() : debug_;
    }
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    private void setDebug(com.treeleaf.anydone.entities.TreeleafProto.Debug value) {
      if (value == null) {
        throw new NullPointerException();
      }
      debug_ = value;
      
      }
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    private void setDebug(
        com.treeleaf.anydone.entities.TreeleafProto.Debug.Builder builderForValue) {
      debug_ = builderForValue.build();
      
    }
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    private void mergeDebug(com.treeleaf.anydone.entities.TreeleafProto.Debug value) {
      if (debug_ != null &&
          debug_ != com.treeleaf.anydone.entities.TreeleafProto.Debug.getDefaultInstance()) {
        debug_ =
          com.treeleaf.anydone.entities.TreeleafProto.Debug.newBuilder(debug_).mergeFrom(value).buildPartial();
      } else {
        debug_ = value;
      }
      
    }
    /**
     * <code>optional .treeleaf.protos.Debug debug = 6;</code>
     */
    private void clearDebug() {  debug_ = null;
      
    }

    public static final int EVENTGENERATEDHOST_FIELD_NUMBER = 7;
    private java.lang.String eventGeneratedHost_;
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    public java.lang.String getEventGeneratedHost() {
      return eventGeneratedHost_;
    }
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    public com.google.protobuf.ByteString
        getEventGeneratedHostBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(eventGeneratedHost_);
    }
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    private void setEventGeneratedHost(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      eventGeneratedHost_ = value;
    }
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    private void clearEventGeneratedHost() {
      
      eventGeneratedHost_ = getDefaultInstance().getEventGeneratedHost();
    }
    /**
     * <pre>
     *Ex: prod1-txn
     * </pre>
     *
     * <code>optional string eventGeneratedHost = 7;</code>
     */
    private void setEventGeneratedHostBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      eventGeneratedHost_ = value.toStringUtf8();
    }

    public static final int EMITTER_FIELD_NUMBER = 8;
    private java.lang.String emitter_;
    /**
     * <code>optional string emitter = 8;</code>
     */
    public java.lang.String getEmitter() {
      return emitter_;
    }
    /**
     * <code>optional string emitter = 8;</code>
     */
    public com.google.protobuf.ByteString
        getEmitterBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(emitter_);
    }
    /**
     * <code>optional string emitter = 8;</code>
     */
    private void setEmitter(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      emitter_ = value;
    }
    /**
     * <code>optional string emitter = 8;</code>
     */
    private void clearEmitter() {
      
      emitter_ = getDefaultInstance().getEmitter();
    }
    /**
     * <code>optional string emitter = 8;</code>
     */
    private void setEmitterBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      emitter_ = value.toStringUtf8();
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (type_ != com.treeleaf.anydone.entities.EventsProto.EventType.UNKNOWN_EVENT_TYPE.getNumber()) {
        output.writeEnum(1, type_);
      }
      if (immediate_ != false) {
        output.writeBool(2, immediate_);
      }
      if (!payload_.isEmpty()) {
        output.writeBytes(3, payload_);
      }
      if (timestamp_ != 0L) {
        output.writeInt64(4, timestamp_);
      }
      if (!eventId_.isEmpty()) {
        output.writeString(5, getEventId());
      }
      if (debug_ != null) {
        output.writeMessage(6, getDebug());
      }
      if (!eventGeneratedHost_.isEmpty()) {
        output.writeString(7, getEventGeneratedHost());
      }
      if (!emitter_.isEmpty()) {
        output.writeString(8, getEmitter());
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (type_ != com.treeleaf.anydone.entities.EventsProto.EventType.UNKNOWN_EVENT_TYPE.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, type_);
      }
      if (immediate_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, immediate_);
      }
      if (!payload_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, payload_);
      }
      if (timestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(4, timestamp_);
      }
      if (!eventId_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getEventId());
      }
      if (debug_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, getDebug());
      }
      if (!eventGeneratedHost_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getEventGeneratedHost());
      }
      if (!emitter_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(8, getEmitter());
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.EventsProto.Event parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.treeleaf.anydone.entities.EventsProto.Event prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * <pre>
     *Generic Event object
     * </pre>
     *
     * Protobuf type {@code oyster.entities.pb.Event}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.treeleaf.anydone.entities.EventsProto.Event, Builder> implements
        // @@protoc_insertion_point(builder_implements:oyster.entities.pb.Event)
        com.treeleaf.anydone.entities.EventsProto.EventOrBuilder {
      // Construct using com.treeleaf.anydone.entities.EventsProto.Event.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>optional .oyster.entities.pb.EventType type = 1;</code>
       */
      public int getTypeValue() {
        return instance.getTypeValue();
      }
      /**
       * <code>optional .oyster.entities.pb.EventType type = 1;</code>
       */
      public Builder setTypeValue(int value) {
        copyOnWrite();
        instance.setTypeValue(value);
        return this;
      }
      /**
       * <code>optional .oyster.entities.pb.EventType type = 1;</code>
       */
      public com.treeleaf.anydone.entities.EventsProto.EventType getType() {
        return instance.getType();
      }
      /**
       * <code>optional .oyster.entities.pb.EventType type = 1;</code>
       */
      public Builder setType(com.treeleaf.anydone.entities.EventsProto.EventType value) {
        copyOnWrite();
        instance.setType(value);
        return this;
      }
      /**
       * <code>optional .oyster.entities.pb.EventType type = 1;</code>
       */
      public Builder clearType() {
        copyOnWrite();
        instance.clearType();
        return this;
      }

      /**
       * <code>optional bool immediate = 2;</code>
       */
      public boolean getImmediate() {
        return instance.getImmediate();
      }
      /**
       * <code>optional bool immediate = 2;</code>
       */
      public Builder setImmediate(boolean value) {
        copyOnWrite();
        instance.setImmediate(value);
        return this;
      }
      /**
       * <code>optional bool immediate = 2;</code>
       */
      public Builder clearImmediate() {
        copyOnWrite();
        instance.clearImmediate();
        return this;
      }

      /**
       * <pre>
       *All general payload.
       * </pre>
       *
       * <code>optional bytes payload = 3;</code>
       */
      public com.google.protobuf.ByteString getPayload() {
        return instance.getPayload();
      }
      /**
       * <pre>
       *All general payload.
       * </pre>
       *
       * <code>optional bytes payload = 3;</code>
       */
      public Builder setPayload(com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setPayload(value);
        return this;
      }
      /**
       * <pre>
       *All general payload.
       * </pre>
       *
       * <code>optional bytes payload = 3;</code>
       */
      public Builder clearPayload() {
        copyOnWrite();
        instance.clearPayload();
        return this;
      }

      /**
       * <pre>
       *Used on reliable queue
       * </pre>
       *
       * <code>optional int64 timestamp = 4;</code>
       */
      public long getTimestamp() {
        return instance.getTimestamp();
      }
      /**
       * <pre>
       *Used on reliable queue
       * </pre>
       *
       * <code>optional int64 timestamp = 4;</code>
       */
      public Builder setTimestamp(long value) {
        copyOnWrite();
        instance.setTimestamp(value);
        return this;
      }
      /**
       * <pre>
       *Used on reliable queue
       * </pre>
       *
       * <code>optional int64 timestamp = 4;</code>
       */
      public Builder clearTimestamp() {
        copyOnWrite();
        instance.clearTimestamp();
        return this;
      }

      /**
       * <code>optional string eventId = 5;</code>
       */
      public java.lang.String getEventId() {
        return instance.getEventId();
      }
      /**
       * <code>optional string eventId = 5;</code>
       */
      public com.google.protobuf.ByteString
          getEventIdBytes() {
        return instance.getEventIdBytes();
      }
      /**
       * <code>optional string eventId = 5;</code>
       */
      public Builder setEventId(
          java.lang.String value) {
        copyOnWrite();
        instance.setEventId(value);
        return this;
      }
      /**
       * <code>optional string eventId = 5;</code>
       */
      public Builder clearEventId() {
        copyOnWrite();
        instance.clearEventId();
        return this;
      }
      /**
       * <code>optional string eventId = 5;</code>
       */
      public Builder setEventIdBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setEventIdBytes(value);
        return this;
      }

      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public boolean hasDebug() {
        return instance.hasDebug();
      }
      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public com.treeleaf.anydone.entities.TreeleafProto.Debug getDebug() {
        return instance.getDebug();
      }
      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public Builder setDebug(com.treeleaf.anydone.entities.TreeleafProto.Debug value) {
        copyOnWrite();
        instance.setDebug(value);
        return this;
        }
      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public Builder setDebug(
          com.treeleaf.anydone.entities.TreeleafProto.Debug.Builder builderForValue) {
        copyOnWrite();
        instance.setDebug(builderForValue);
        return this;
      }
      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public Builder mergeDebug(com.treeleaf.anydone.entities.TreeleafProto.Debug value) {
        copyOnWrite();
        instance.mergeDebug(value);
        return this;
      }
      /**
       * <code>optional .treeleaf.protos.Debug debug = 6;</code>
       */
      public Builder clearDebug() {  copyOnWrite();
        instance.clearDebug();
        return this;
      }

      /**
       * <pre>
       *Ex: prod1-txn
       * </pre>
       *
       * <code>optional string eventGeneratedHost = 7;</code>
       */
      public java.lang.String getEventGeneratedHost() {
        return instance.getEventGeneratedHost();
      }
      /**
       * <pre>
       *Ex: prod1-txn
       * </pre>
       *
       * <code>optional string eventGeneratedHost = 7;</code>
       */
      public com.google.protobuf.ByteString
          getEventGeneratedHostBytes() {
        return instance.getEventGeneratedHostBytes();
      }
      /**
       * <pre>
       *Ex: prod1-txn
       * </pre>
       *
       * <code>optional string eventGeneratedHost = 7;</code>
       */
      public Builder setEventGeneratedHost(
          java.lang.String value) {
        copyOnWrite();
        instance.setEventGeneratedHost(value);
        return this;
      }
      /**
       * <pre>
       *Ex: prod1-txn
       * </pre>
       *
       * <code>optional string eventGeneratedHost = 7;</code>
       */
      public Builder clearEventGeneratedHost() {
        copyOnWrite();
        instance.clearEventGeneratedHost();
        return this;
      }
      /**
       * <pre>
       *Ex: prod1-txn
       * </pre>
       *
       * <code>optional string eventGeneratedHost = 7;</code>
       */
      public Builder setEventGeneratedHostBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setEventGeneratedHostBytes(value);
        return this;
      }

      /**
       * <code>optional string emitter = 8;</code>
       */
      public java.lang.String getEmitter() {
        return instance.getEmitter();
      }
      /**
       * <code>optional string emitter = 8;</code>
       */
      public com.google.protobuf.ByteString
          getEmitterBytes() {
        return instance.getEmitterBytes();
      }
      /**
       * <code>optional string emitter = 8;</code>
       */
      public Builder setEmitter(
          java.lang.String value) {
        copyOnWrite();
        instance.setEmitter(value);
        return this;
      }
      /**
       * <code>optional string emitter = 8;</code>
       */
      public Builder clearEmitter() {
        copyOnWrite();
        instance.clearEmitter();
        return this;
      }
      /**
       * <code>optional string emitter = 8;</code>
       */
      public Builder setEmitterBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setEmitterBytes(value);
        return this;
      }

      // @@protoc_insertion_point(builder_scope:oyster.entities.pb.Event)
    }
    protected final Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.treeleaf.anydone.entities.EventsProto.Event();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          com.treeleaf.anydone.entities.EventsProto.Event other = (com.treeleaf.anydone.entities.EventsProto.Event) arg1;
          type_ = visitor.visitInt(type_ != 0, type_,    other.type_ != 0, other.type_);
          immediate_ = visitor.visitBoolean(immediate_ != false, immediate_,
              other.immediate_ != false, other.immediate_);
          payload_ = visitor.visitByteString(payload_ != com.google.protobuf.ByteString.EMPTY, payload_,
              other.payload_ != com.google.protobuf.ByteString.EMPTY, other.payload_);
          timestamp_ = visitor.visitLong(timestamp_ != 0L, timestamp_,
              other.timestamp_ != 0L, other.timestamp_);
          eventId_ = visitor.visitString(!eventId_.isEmpty(), eventId_,
              !other.eventId_.isEmpty(), other.eventId_);
          debug_ = visitor.visitMessage(debug_, other.debug_);
          eventGeneratedHost_ = visitor.visitString(!eventGeneratedHost_.isEmpty(), eventGeneratedHost_,
              !other.eventGeneratedHost_.isEmpty(), other.eventGeneratedHost_);
          emitter_ = visitor.visitString(!emitter_.isEmpty(), emitter_,
              !other.emitter_.isEmpty(), other.emitter_);
          if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
              .INSTANCE) {
          }
          return this;
        }
        case MERGE_FROM_STREAM: {
          com.google.protobuf.CodedInputStream input =
              (com.google.protobuf.CodedInputStream) arg0;
          com.google.protobuf.ExtensionRegistryLite extensionRegistry =
              (com.google.protobuf.ExtensionRegistryLite) arg1;
          try {
            boolean done = false;
            while (!done) {
              int tag = input.readTag();
              switch (tag) {
                case 0:
                  done = true;
                  break;
                default: {
                  if (!input.skipField(tag)) {
                    done = true;
                  }
                  break;
                }
                case 8: {
                  int rawValue = input.readEnum();

                  type_ = rawValue;
                  break;
                }
                case 16: {

                  immediate_ = input.readBool();
                  break;
                }
                case 26: {

                  payload_ = input.readBytes();
                  break;
                }
                case 32: {

                  timestamp_ = input.readInt64();
                  break;
                }
                case 42: {
                  String s = input.readStringRequireUtf8();

                  eventId_ = s;
                  break;
                }
                case 50: {
                  com.treeleaf.anydone.entities.TreeleafProto.Debug.Builder subBuilder = null;
                  if (debug_ != null) {
                    subBuilder = debug_.toBuilder();
                  }
                  debug_ = input.readMessage(com.treeleaf.anydone.entities.TreeleafProto.Debug.parser(), extensionRegistry);
                  if (subBuilder != null) {
                    subBuilder.mergeFrom(debug_);
                    debug_ = subBuilder.buildPartial();
                  }

                  break;
                }
                case 58: {
                  String s = input.readStringRequireUtf8();

                  eventGeneratedHost_ = s;
                  break;
                }
                case 66: {
                  String s = input.readStringRequireUtf8();

                  emitter_ = s;
                  break;
                }
              }
            }
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException(e.setUnfinishedMessage(this));
          } catch (java.io.IOException e) {
            throw new RuntimeException(
                new com.google.protobuf.InvalidProtocolBufferException(
                    e.getMessage()).setUnfinishedMessage(this));
          } finally {
          }
        }
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          if (PARSER == null) {    synchronized (com.treeleaf.anydone.entities.EventsProto.Event.class) {
              if (PARSER == null) {
                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
              }
            }
          }
          return PARSER;
        }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:oyster.entities.pb.Event)
    private static final com.treeleaf.anydone.entities.EventsProto.Event DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Event();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static com.treeleaf.anydone.entities.EventsProto.Event getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<Event> PARSER;

    public static com.google.protobuf.Parser<Event> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
