package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.Bindable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class BaseWorld
  extends Bindable
  implements IWorld
{
  protected final HashMap<String, EngineObject> objects;
  protected EngineObject player;

  protected BaseWorld()
  {
    objects = new HashMap<>();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    ArrayList<EngineObject> disposed;
    synchronized (objects)
    {
      disposed = new ArrayList<>(objects.values());
      objects.clear();
    }

    for (EngineObject object : disposed)
      object.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    ArrayList<EngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (EngineObject object : enabling)
      object.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    ArrayList<EngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (EngineObject object : enabling)
      object.disable();
  }

  @Override
  public void fillRenderable(List<I3DRenderable> filled)
  {
    synchronized (objects)
    {
      for (EngineObject object : objects.values())
      {
        object.setGlobalRenderablePosition();
        Collections.addAll(filled, object.renderables);
      }
    }
  }

  @Override
  public void fillObjects(List<EngineObject> filled)
  {
    synchronized (objects)
    {
      for (EngineObject object : objects.values())
        filled.add(object);
    }
  }

  @Override
  public EngineObject getPlayer() { return player; }

  @Override
  public void add(EngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.put(engineObject.getName(), engineObject);
    }
    engineObject.initialize();
  }

  @Override
  public void remove(EngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.remove(engineObject.getName());
    }
    engineObject.uninitialize();
  }
}
